package com.example.productmanagementex.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.productmanagementex.domain.Category;
import com.example.productmanagementex.domain.Item;

@Repository
public class ItemRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final ResultSetExtractor<List<Item>> ITEM_RESULTSET = (rs) -> {
        List<Item> itemList = new ArrayList<>();
        List<Category> categoryList = null;
        Item item = null;
        int itemBefore = 0;

        while (rs.next()) {
            int itemNow = rs.getInt("i_id");

            if (itemNow != itemBefore) {
                if (item != null) {
                    itemList.add(item);
                }
                categoryList = new ArrayList<>();
                item = new Item();
                item.setId(rs.getInt("i_id"));
                item.setName(rs.getString("i_name"));
                item.setCondition(rs.getInt("i_condition"));
                item.setBrand(rs.getString("i_brand"));
                item.setPrice(rs.getDouble("i_price"));
                item.setStock(rs.getInt("i_stock"));
                item.setShipping(rs.getInt("i_shipping"));
                item.setDescription(rs.getString("i_description"));
                itemBefore = itemNow;
            }
            if (rs.getInt("c_id") != 0) {
                Category category = new Category();
                category.setId(rs.getInt("c_id"));
                category.setParentId(rs.getInt("c_parent_id"));
                category.setName(rs.getString("c_name"));
                category.setNameAll(rs.getString("c_name_all"));
                category.setCategoryNumber(rs.getInt("c_category_number"));
                categoryList.add(category);
            }
            item.setCategory(categoryList);
        }
        if (item != null) {
            itemList.add(item);
        }
        return itemList;
    };

    private final String FIND_ALL_SQL = """
            SELECT
                i.id AS i_id,
                i.name AS i_name,
                i.condition AS i_condition,
                i.category AS i_category,
                i.brand AS i_brand,
                i.price AS i_price,
                i.stock AS i_stock,
                i.shipping AS i_shipping,
                i.description AS i_description,
                c.id AS c_id,
                c.parent_id AS c_parent_id,
                c.name AS c_name,
                c.name_all AS c_name_all,
                c.category_number AS c_category_number
            FROM
                items AS i
            LEFT OUTER JOIN
                category AS c
            ON
                i.category = c.category_number
            ORDER BY
                i.id, c.parent_id NULLS FIRST
            ;
            """;

    private final String SEARCH_SQL = """
            SELECT
                i.id AS i_id,
                i.name AS i_name,
                i.condition AS i_condition,
                i.category AS i_category,
                i.brand AS i_brand,
                i.price AS i_price,
                i.stock AS i_stock,
                i.shipping AS i_shipping,
                i.description AS i_description,
                c.id AS c_id,
                c.parent_id AS c_parent_id,
                c.name AS c_name,
                c.name_all AS c_name_all,
                c.category_number AS c_category_number
            FROM
                items AS i
            LEFT OUTER JOIN
                category AS c
            ON
                i.category = c.category_number
            WHERE
                (:name IS NULL OR i.name LIKE :name)
                AND (:brand IS NULL OR i.brand LIKE :brand)
                AND (:parentCategory IS NULL OR i.name_all LIKE :parentCategory)
                AND (:childCategory IS NULL OR i.name_all LIKE :childCategory)
                AND (:grandCategory IS NULL OR i.name_all LIKE :grandCategory)
            ORDER BY
                i.id, c.parent_id NULLS FIRST
            ;
            """;

    public List<Item> findAllItems() {
        SqlParameterSource param = new MapSqlParameterSource();
        List<Item> itemList = template.query(FIND_ALL_SQL, param, ITEM_RESULTSET);
        return itemList;
    }

    public List<Item> searchItems(String name, String brand, String parentCategory, String childCategory,
            String grandCategory) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
                .addValue("brand", "%" + brand + "%")
                .addValue("parentCategory", "%" + parentCategory + "%")
                .addValue("childCategory", "%" + childCategory + "%")
                .addValue("grandCategory", "%" + grandCategory + "%");
        List<Item> itemList = template.query(SEARCH_SQL, param, ITEM_RESULTSET);

        return itemList;
    }

}
