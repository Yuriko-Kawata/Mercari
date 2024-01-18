package com.example.productmanagementex.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.productmanagementex.domain.Category;

@Repository
public class CategoryRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<Category> CATEGORY_ROWMAPPER = (rs, i) -> {
        Category category = new Category();
        category.setId(rs.getInt("id"));
        category.setParentId(rs.getInt("parent_id"));
        category.setName(rs.getString("name"));
        category.setNameAll(rs.getString("name_all"));
        category.setCategoryNumber(rs.getInt("category_number"));
        return category;
    };

    private final String FIND_ALL_SQL = """
            SELECT
                id, parent_id, name, name_all, category_number
            FROM
                category
            ORDER BY
                name, id
            ;
            """;

    private final String CHECK_CATEGORY_SQL = """
            SELECT count(*) FROM category WHERE name_all = :nameAll;
            """;

    private final String INSERT_SQL = """
            INSERT INTO category (name, parent_id, name_all, category_number)
            SELECT
                SPLIT_PART(:nameAll, '/', position),
                CASE
                WHEN position = 1 THEN NULL
                WHEN position = 2 THEN
                    (SELECT id FROM category WHERE name = :parentCategory ORDER BY id LIMIT 1)
                ELSE
                    (SELECT id FROM category WHERE name = :childCategory ORDER BY id LIMIT 1)
                END,
                CASE
                WHEN position = 1 OR position = 2 THEN NULL
                ELSE :nameAll
                END,
                (SELECT category_number FROM category ORDER BY id DESC LIMIT 1 OFFSET 2)+1
            FROM
                generate_series(1, 3) AS position
            ;
            """;

    private static final String FIND_ALL_PARENT_SQL = """
            SELECT
                id, name, parent_id, name_all, category_number
            FROM
                category
            WHERE
                parent_id IS NULL AND name_all IS NULL
            ORDER BY
                id
            LIMIT
                30
            OFFSET
                (:page - 1)* 30
            ;
            """;

    private static final String FIND_ALL_CHILD_SQL = """
            SELECT
                id, name, parent_id, name_all, category_number
            FROM
                category
            WHERE
                parent_id IS NOT NULL AND name_all IS NULL
            ORDER BY
                id
            LIMIT
                30
            OFFSET
                (:page - 1)* 30
            ;
            """;

    private static final String FIND_ALL_GRAND_SQL = """
            SELECT
                id, name, parent_id, name_all, category_number
            FROM
                category
            WHERE
                name_all IS NOT NULL
            ORDER BY
                id
            LIMIT
                30
            OFFSET
                (:page - 1)* 30
            ;
            """;

    private static final String PARENT_SIZE_SQL = """
            SELECT
                count(*)
            FROM
                category
            WHERE
                parent_id IS NULL AND name_all IS NULL
            ;
            """;

    private static final String CHILD_SIZE_SQL = """
            SELECT
                count(*)
            FROM
                category
            WHERE
                parent_id IS NOT NULL AND name_all IS NULL
            ;
            """;

    private static final String GRAND_SIZE_SQL = """
            SELECT
                count(*)
            FROM
                category
            WHERE
                name_all IS NOT NULL
            ;
            """;

    private static final String SEARCH_PARENT_SQL = """
            SELECT
                id, name, parent_id, name_all, category_number
            FROM
            category
            WHERE
                parent_id IS NULL AND name_all IS NULL
                AND name LIKE :name
            ORDER BY
                id
            LIMIT
                30
            OFFSET
                (:page - 1)* 30
            ;
            """;

    private static final String SEARCH_CHILD_SQL = """
            SELECT
                id, name, parent_id, name_all, category_number
            FROM
            category
            WHERE
                parent_id IS NOT NULL AND name_all IS NULL
                AND name LIKE :name
            ORDER BY
                id
            LIMIT
                30
            OFFSET
                (:page - 1)* 30
            ;
            """;

    private static final String SEARCH_GRAND_SQL = """
            SELECT
                id, name, parent_id, name_all, category_number
            FROM
            category
            WHERE
                name_all IS NOT NULL
                AND name LIKE :name
            ORDER BY
                id
            LIMIT
                30
            OFFSET
                (:page - 1)* 30
            ;
            """;

    private static final String SEARCH_PARENT_SIZE_SQL = """
            SELECT
                count(*)
            FROM
            category
            WHERE
                parent_id IS NULL AND name_all IS NULL
                AND name LIKE :name
            ;
            """;

    private static final String SEARCH_CHILD_SIZE_SQL = """
            SELECT
                count(*)
            FROM
            category
            WHERE
                parent_id IS NOT NULL AND name_all IS NULL
                AND name LIKE :name
            ;
            """;

    private static final String SEARCH_GRAND_SIZE_SQL = """
            SELECT
                count(*)
            FROM
            category
            WHERE
                name_all IS NOT NULL
                AND name LIKE :name
            ;
            """;

    public List<Category> findAllUniqueCategory() {
        SqlParameterSource param = new MapSqlParameterSource();
        List<Category> categoryList = template.query(FIND_ALL_SQL, param, CATEGORY_ROWMAPPER);
        return categoryList;
    }

    private int toCheckCategory(String nameAll) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("nameAll", nameAll);
        int categoryCount = template.queryForObject(CHECK_CATEGORY_SQL, param, Integer.class);
        return categoryCount;
    }

    public void checkCategory(String parentCategory, String childCategory, String grandCategory, String nameAll) {
        if (toCheckCategory(nameAll) == 0) {
            SqlParameterSource param = new MapSqlParameterSource().addValue("parentCategory", parentCategory)
                    .addValue("childCategory", childCategory).addValue("grandCategory", grandCategory)
                    .addValue("nameAll", nameAll);
            template.update(INSERT_SQL, param);
        }
    }

    public List<Category> findAllParentCategory(int page) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("page", page);
        List<Category> categoryList = template.query(FIND_ALL_PARENT_SQL, param, CATEGORY_ROWMAPPER);
        return categoryList;
    }

    public List<Category> findAllChildCategory(int page) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("page", page);
        List<Category> categoryList = template.query(FIND_ALL_CHILD_SQL, param, CATEGORY_ROWMAPPER);
        return categoryList;
    }

    public List<Category> findAllGrandCategory(int page) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("page", page);
        List<Category> categoryList = template.query(FIND_ALL_GRAND_SQL, param, CATEGORY_ROWMAPPER);
        return categoryList;
    }

    public int parentListSize() {
        SqlParameterSource param = new MapSqlParameterSource();
        int size = template.queryForObject(PARENT_SIZE_SQL, param, Integer.class);
        return size;
    }

    public int childListSize() {
        SqlParameterSource param = new MapSqlParameterSource();
        int size = template.queryForObject(CHILD_SIZE_SQL, param, Integer.class);
        return size;
    }

    public int grandListSize() {
        SqlParameterSource param = new MapSqlParameterSource();
        int size = template.queryForObject(GRAND_SIZE_SQL, param, Integer.class);
        return size;
    }

    public List<Category> searchParentCategory(String condition, int page) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", condition).addValue("page", page);
        List<Category> categoryList = template.query(SEARCH_PARENT_SQL, param, CATEGORY_ROWMAPPER);
        return categoryList;
    }

    public List<Category> searchChildCategory(String condition, int page) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", condition).addValue("page", page);
        List<Category> categoryList = template.query(SEARCH_CHILD_SQL, param, CATEGORY_ROWMAPPER);
        return categoryList;
    }

    public List<Category> searchGrandCategory(String condition, int page) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", condition).addValue("page", page);
        List<Category> categoryList = template.query(SEARCH_GRAND_SQL, param, CATEGORY_ROWMAPPER);
        return categoryList;
    }

    public int searchParentTotalPage(String codition) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", codition);
        int size = template.queryForObject(SEARCH_PARENT_SIZE_SQL, param, Integer.class);
        return size;
    }

    public int searchChildTotalPage(String codition) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", codition);
        int size = template.queryForObject(SEARCH_CHILD_SIZE_SQL, param, Integer.class);
        return size;
    }

    public int searchGrandTotalPage(String codition) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", codition);
        int size = template.queryForObject(SEARCH_GRAND_SIZE_SQL, param, Integer.class);
        return size;
    }

}
