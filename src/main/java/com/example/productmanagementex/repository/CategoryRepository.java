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

}
