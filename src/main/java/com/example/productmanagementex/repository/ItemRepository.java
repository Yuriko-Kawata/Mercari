package com.example.productmanagementex.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.productmanagementex.domain.Category;
import com.example.productmanagementex.domain.Item;

import java.sql.Timestamp;

@Repository
public class ItemRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private static RowMapper<Item> ITEM_ROWMAPPER = (rs, i) -> {
        Item item = new Item();
        item.setId(rs.getInt("i_id"));
        item.setName(rs.getString("i_name"));
        item.setCondition(rs.getInt("i_condition"));
        item.setBrand(rs.getString("i_brand"));
        item.setPrice(rs.getDouble("i_price"));
        item.setStock(rs.getInt("i_stock"));
        item.setShipping(rs.getInt("i_shipping"));
        item.setDescription(rs.getString("i_description"));
        item.setUpdateTime(rs.getDate("i_update_time"));
        item.setDelFlg(rs.getInt("i_del_flg"));

        List<Category> categoryList = new ArrayList<>();
        Category parentCategory = new Category();
        parentCategory.setId(rs.getInt("parent_id"));
        parentCategory.setName(rs.getString("parent_name"));
        parentCategory.setParentId(rs.getInt("parent_parent_id"));
        parentCategory.setNameAll(rs.getString("parent_name_all"));
        categoryList.add(parentCategory);
        Category childCategory = new Category();
        childCategory.setId(rs.getInt("child_id"));
        childCategory.setName(rs.getString("child_name"));
        childCategory.setParentId(rs.getInt("child_parent_id"));
        childCategory.setNameAll(rs.getString("child_name_all"));
        categoryList.add(childCategory);
        Category grandCategory = new Category();
        grandCategory.setId(rs.getInt("grand_id"));
        grandCategory.setName(rs.getString("grand_name"));
        grandCategory.setParentId(rs.getInt("grand_parent_id"));
        grandCategory.setNameAll(rs.getString("grand_name_all"));
        categoryList.add(grandCategory);

        item.setCategory(categoryList);
        return item;
    };

    // private static final ResultSetExtractor<List<Item>> ITEM_RESULTSET = (rs) ->
    // {
    // List<Item> itemList = new ArrayList<>();
    // List<Category> categoryList = null;
    // Item item = null;
    // int itemBefore = 0;

    // while (rs.next()) {
    // int itemNow = rs.getInt("i_id");

    // if (itemNow != itemBefore) {
    // if (item != null) {
    // itemList.add(item);
    // }
    // categoryList = new ArrayList<>();
    // item = new Item();
    // item.setId(rs.getInt("i_id"));
    // item.setName(rs.getString("i_name"));
    // item.setCondition(rs.getInt("i_condition"));
    // item.setBrand(rs.getString("i_brand"));
    // item.setPrice(rs.getDouble("i_price"));
    // item.setStock(rs.getInt("i_stock"));
    // item.setShipping(rs.getInt("i_shipping"));
    // item.setDescription(rs.getString("i_description"));
    // item.setUpdateTime(rs.getDate("i_update_time"));
    // item.setDelFlg(rs.getInt("i_del_flg"));
    // itemBefore = itemNow;
    // }
    // if (rs.getInt("c_id") != 0) {
    // Category category = new Category();
    // category.setId(rs.getInt("c_id"));
    // category.setParentId(rs.getInt("c_parent_id"));
    // category.setName(rs.getString("c_name"));
    // category.setNameAll(rs.getString("c_name_all"));
    // categoryList.add(category);
    // }
    // item.setCategory(categoryList);
    // }
    // if (item != null) {
    // itemList.add(item);
    // }
    // return itemList;
    // };

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
                i.update_time AS i_update_time,
                i.del_flg AS i_del_flg,
                grand.id AS grand_id,
                grand.name AS grand_name,
                grand.parent_id AS grand_parent_id,
                grand.name_all AS grand_name_all,
                child.id AS child_id,
                child.name AS child_name,
                child.parent_id AS child_parent_id,
                child.name_all AS child_name_all,
                parent.id AS parent_id,
                parent.name AS parent_name,
                parent.parent_id AS parent_parent_id,
                parent.name_all AS parent_name_all
            FROM
                items AS i
            LEFT OUTER JOIN
                category AS grand
            ON
                i.category = grand.id
            LEFT OUTER JOIN
                category AS child
            ON
                grand.parent_id = child.id
            LEFT OUTER JOIN
                category AS parent
            ON
                child.parent_id = parent.id
            WHERE
                    i.del_flg = 0
            ORDER BY
                i.id
            LIMIT
                30
            OFFSET
                (:page - 1) * 30
            ;
            """;

    private final String ITEMLIST_SIZE_SQL = """
            SELECT
                count(*)
            from
                items
            WHERE
                del_flg = 0
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
                i.update_time AS i_update_time,
                i.del_flg AS i_del_flg,
                grand.id AS grand_id,
                grand.name AS grand_name,
                grand.parent_id AS grand_parent_id,
                grand.name_all AS grand_name_all,
                child.id AS child_id,
                child.name AS child_name,
                child.parent_id AS child_parent_id,
                child.name_all AS child_name_all,
                parent.id AS parent_id,
                parent.name AS parent_name,
                parent.parent_id AS parent_parent_id,
                parent.name_all AS parent_name_all
            FROM
                items AS i
            LEFT OUTER JOIN
                category AS grand
            ON
                i.category = grand.id
            LEFT OUTER JOIN
                category AS child
            ON
                grand.parent_id = child.id
            LEFT OUTER JOIN
                category AS parent
            ON
                child.parent_id = parent.id
            WHERE
                i.del_flg = 0
                AND (i.name LIKE :name)
                AND (i.brand LIKE :brand)
                AND  i.category IN
                    (SELECT
                        id
                    FROM
                        category
                    WHERE
                        name_all LIKE :nameAll
                        )
            ORDER BY
                i.id
            LIMIT
                30
            OFFSET
                (:page - 1) * 30
            ;
            """;

    private static final String SEARCH_ITEM_LIST_SIZE_SQL = """
            SELECT
                count(*)
            FROM
                items
            WHERE
                del_flg = 0
                AND(name LIKE :name)
                AND (brand LIKE :brand)
                AND  category IN
                    (SELECT
                        id
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
                i.update_time AS i_update_time,
                i.del_flg AS i_del_flg,
                grand.id AS grand_id,
                grand.name AS grand_name,
                grand.parent_id AS grand_parent_id,
                grand.name_all AS grand_name_all,
                child.id AS child_id,
                child.name AS child_name,
                child.parent_id AS child_parent_id,
                child.name_all AS child_name_all,
                parent.id AS parent_id,
                parent.name AS parent_name,
                parent.parent_id AS parent_parent_id,
                parent.name_all AS parent_name_all
            FROM
                items AS i
            LEFT OUTER JOIN
                category AS grand
            ON
                i.category = grand.id
            LEFT OUTER JOIN
                category AS child
            ON
                grand.parent_id = child.id
            LEFT OUTER JOIN
                category AS parent
            ON
                child.parent_id = parent.id
            WHERE
                i.id = :id
            ;
            """;

    private static final String INSERT_SQL = """
            INSERT INTO
                items(name, condition, category, brand, price, description)
            VALUES
                (:name, :condition,
                (SELECT
                    id
                FROM
                    category
                WHERE
                    name_all = :nameAll)
                ,
                :brand, :price, :description
                )
            ;
            """;

    private static final String UPDATE_SQL = """
            UPDATE
                items
            SET
                name = :name, condition = :condition,
                category = (SELECT id FROM category WHERE name_all = :nameAll),
                brand = :brand, price = :price, description = :description
            WHERE
                id = :id
            ;
            """;

    private static final String GET_UPDATETIME_SQL = """
            SELECT
                update_time
            FROM
                items
            WHERE
                id = :id
            ;
            """;

    private static final String CHECK_DELETE_SQL = """
            SELECT
                count(*)
            FROM
                items
            WHERE
                id = :id AND update_time = :updateTime AND del_flg = 0
            ;
            """;

    private static final String CHANGE_DELETE_SQL = """
            UPDATE
                items
            SET
                del_flg = CASE WHEN del_flg = 0 THEN 1 ELSE 0 END
            WHERE
                id = :id
            ;
            """;

    private static final String UPDATE_CATEGORY_SQL = """
            UPDATE
                items
            SET
                category = (
                    SELECT
                        id
                    FROM
                        category
                    WHERE
                        name_all = 'カテゴリ無/カテゴリ無/カテゴリ無'
                ),
                update_time =
            WHERE
                category = :id
            ;
            """;

    public List<Item> findAllItems(int page) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("page", page);
        List<Item> itemList = template.query(FIND_ALL_SQL, param, ITEM_ROWMAPPER);
        return itemList;
    }

    public Integer itemListSize() {
        SqlParameterSource param = new MapSqlParameterSource();
        Integer itemsize = template.queryForObject(ITEMLIST_SIZE_SQL, param, Integer.class);
        return itemsize;
    }

    public List<Item> searchItems(String name, String brand, String nameAll, int page) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", name).addValue("brand", brand)
                .addValue("nameAll", nameAll).addValue("page", page);
        List<Item> itemList = template.query(SEARCH_SQL, param, ITEM_ROWMAPPER);
        return itemList;
    }

    public Integer searchItemsSize(String name, String brand, String nameAll) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", name).addValue("brand", brand)
                .addValue("nameAll", nameAll);
        Integer itemListSize = template.queryForObject(SEARCH_ITEM_LIST_SIZE_SQL, param, Integer.class);
        return itemListSize;
    }

    public Item findById(int id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        List<Item> itemList = template.query(FIND_BY_ID_SQL, param, ITEM_ROWMAPPER);
        Item item = itemList.get(0);
        return item;
    }

    public void insertItem(Item item, String nameAll) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", item.getName())
                .addValue("condition", item.getCondition()).addValue("brand", item.getBrand())
                .addValue("price", item.getPrice()).addValue("description", item.getDescription())
                .addValue("nameAll", nameAll);
        template.update(INSERT_SQL, param);
    }

    public void updateItem(Item item, String nameAll) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", item.getId())
                .addValue("name", item.getName())
                .addValue("condition", item.getCondition()).addValue("brand", item.getBrand())
                .addValue("price", item.getPrice()).addValue("description", item.getDescription())
                .addValue("nameAll", nameAll);
        template.update(UPDATE_SQL, param);
    }

    public Timestamp getUpdateTime(int id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        Timestamp updateTime = template.queryForObject(GET_UPDATETIME_SQL, param, Timestamp.class);
        return updateTime;
    }

    public Integer checkDelete(int id, Timestamp updateTime) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id).addValue("updateTime", updateTime);
        Integer count = template.queryForObject(CHECK_DELETE_SQL, param, Integer.class);
        return count;
    }

    public void changeDeleteStatus(int id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        template.update(CHANGE_DELETE_SQL, param);
    }

    public void updateCategory(List<Integer> changeRecordIdList) {
        List<SqlParameterSource> parameters = new ArrayList<>();

        for (Integer changeRecordId : changeRecordIdList) {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("id", changeRecordId);
            parameters.add(param);
        }

        template.batchUpdate(UPDATE_CATEGORY_SQL, parameters.toArray(new SqlParameterSource[0]));
    }

}
