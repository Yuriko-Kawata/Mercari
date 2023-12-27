package com.example.productmanagementex.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.productmanagementex.domain.Item;

@Repository
public class ItemRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<Item> ITEM_ROWMAPPER = (rs, i) -> {
        Item item = new Item();
        item.setId(rs.getInt("id"));
        item.setName(rs.getString("name"));
        item.setCondition(rs.getInt("condition"));
        item.setCategory(rs.getInt("category"));
        item.setBrand(rs.getString("brand"));
        item.setPrice(rs.getDouble("price"));
        item.setStock(rs.getInt("stock"));
        item.setShipping(rs.getInt("shipping"));
        item.setDescription(rs.getString("description"));
        return item;
    };

    private static final String FIND_ALL_SQL = """
            SELECT
             id, name, condition, category, brand, price, stock, shipping, description
            FROM
             items
            ;
            """;

    public List<Item> findAllItems() {
        SqlParameterSource param = new MapSqlParameterSource();
        return template.query(FIND_ALL_SQL, param, ITEM_ROWMAPPER);
    }

}
