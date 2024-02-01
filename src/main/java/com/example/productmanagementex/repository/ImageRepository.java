package com.example.productmanagementex.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.productmanagementex.domain.Image;

@Repository
public class ImageRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<Image> IMAGE_ROWMAPPER = (rs, i) -> {
        Image image = new Image();
        image.setId(rs.getInt("id"));
        image.setItemId(rs.getInt("item_id"));
        image.setPath(rs.getString("path"));
        return image;
    };

    // 新規作成を行うクエリ
    private static final String INSERT_SQL = """
            INSERT INTO
                images(item_id, path)
            VALUES
                (:itemId, :path)
            ;
            """;

    // item idに対応するレコードからpathを取得するクエリ
    private static final String FIND_PATH_BY_ITEM_ID_SQL = """
            SELECT
                path
            FROM
                images
            WHERE
                item_id = :itemId
            ;
            """;

    /**
     * 新規作成
     * 
     * @param itemId item id
     * @param path   path
     */
    public void insert(int itemId, String path) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", itemId).addValue("path", path);
        template.update(INSERT_SQL, param);
    }

    /**
     * item idからpathを取得
     * 
     * @param itemId item id
     * @return path
     */
    public String findPathByItemId(int itemId) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", itemId);
        String path = template.query(FIND_PATH_BY_ITEM_ID_SQL, param, rs -> {
            if (rs.next()) {
                return rs.getString("path");
            }
            return null;
        });
        return path;
    }
}
