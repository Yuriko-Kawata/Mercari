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
            DISTINCT ON(name, parent_id)
                id, parent_id, name, name_all, category_number
            FROM
                category
            ;
            """;

    public List<Category> findAllUniqueCategory() {
        SqlParameterSource param = new MapSqlParameterSource();
        List<Category> categoryList = template.query(FIND_ALL_SQL, param, CATEGORY_ROWMAPPER);
        return categoryList;
    }
}
