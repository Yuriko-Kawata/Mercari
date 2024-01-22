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
                item.setDelete(rs.getBoolean("i_delete"));
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
                i.delete AS i_delete,
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
            LIMIT
                90
            OFFSET
                (:page - 1) * 90
            ;
            """;

    private final String ITEMLIST_SIZE_SQL = "SELECT count(*) from items;";

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
                i.delete AS i_delete,
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
                (i.name LIKE :name)
                AND (i.brand LIKE :brand)
                AND  c.category_number IN
                    (SELECT
                        category_number
                    FROM
                        category
                    WHERE
                        name_all LIKE :nameAll
                        )
            ORDER BY
                i.id, c.parent_id NULLS FIRST
            LIMIT
                90
            OFFSET
                (:page - 1) * 90
            ;
            """;

    private static final String SEARCH_ITEM_LIST_SIZE_SQL = """
            SELECT
                count(*)
            FROM
                items
            WHERE
                (name LIKE :name)
                AND (brand LIKE :brand)
                AND  category IN
                    (SELECT
                        category_number
                    FROM
                        category
                    WHERE
                        name_all LIKE :nameAll
                    )
            ;
            """;

    private static final String FIND_BY_ID_SQL = """
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
                i.delete AS i_delete,
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
                i.id = :id
            ORDER BY
                c.id
            ;
            """;

    private static final String INSERT_SQL = """
            INSERT INTO
                items(name, condition, category, brand, price, description, name_all)
            VALUES
                (:name, :condition,
                (SELECT
                    category_number
                FROM
                    category
                WHERE
                    name_all = :nameAll)
                ,
                :brand, :price, :description, :nameAll
                )
            ;
            """;

    private static final String UPDATE_SQL = """
            UPDATE
                items
            SET
                name = :name, condition = :condition,
                category = (SELECT category_number FROM category WHERE name_all = :nameAll),
                brand = :brand, price = :price, description = :description, name_all = :nameAll
            WHERE
                id = :id
            """;

    private static final String CHANGE_DELETE_SQL = """
            UPDATE
                items
            SET
                delete = NOT delete
            WHERE
                id = :id
            """;

    public List<Item> findAllItems(int page) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("page", page);
        List<Item> itemList = template.query(FIND_ALL_SQL, param, ITEM_RESULTSET);
        return itemList;
    }

    public int itemListSize() {
        SqlParameterSource param = new MapSqlParameterSource();
        return template.queryForObject(ITEMLIST_SIZE_SQL, param, Integer.class);
    }

    public List<Item> searchItems(String name, String brand, String nameAll, int page) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", name).addValue("brand", brand)
                .addValue("nameAll", nameAll).addValue("page", page);
        List<Item> itemList = template.query(SEARCH_SQL, param, ITEM_RESULTSET);
        return itemList;
    }

    public int searchItemsSize(String name, String brand, String nameAll) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", name).addValue("brand", brand)
                .addValue("nameAll", nameAll);
        int itemListSize = template.queryForObject(SEARCH_ITEM_LIST_SIZE_SQL, param, Integer.class);
        return itemListSize;
    }

    public Item findById(int id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        List<Item> itemList = template.query(FIND_BY_ID_SQL, param, ITEM_RESULTSET);
        Item item = itemList.get(0);
        return item;
    }

    public void insertItem(Item item) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", item.getName())
                .addValue("condition", item.getCondition()).addValue("brand", item.getBrand())
                .addValue("price", item.getPrice()).addValue("description", item.getDescription())
                .addValue("nameAll", item.getNameAll());
        template.update(INSERT_SQL, param);
    }

    public void updateItem(Item item) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", item.getId())
                .addValue("name", item.getName())
                .addValue("condition", item.getCondition()).addValue("brand", item.getBrand())
                .addValue("price", item.getPrice()).addValue("description", item.getDescription())
                .addValue("nameAll", item.getNameAll());
        template.update(UPDATE_SQL, param);
    }

    public void changeDeleteStatus(int id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        template.update(CHANGE_DELETE_SQL, param);
    }

}
