package com.example.productmanagementex.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.productmanagementex.domain.User;

/**
 * userのrepositoryクラス
 * 
 * @author hiraizumi
 */
@Repository
public class UserRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final Logger logger = LogManager.getLogger(UserRepository.class);

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, i) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setMail(rs.getString("mail"));
        user.setPassword(rs.getString("password"));
        user.setAuthority(rs.getInt("authority"));
        return user;
    };

    // 新規追加用のクエリ
    private static final String INSERT_SQL = """
            INSERT INTO
                users(name, mail, password, authority)
            VALUES
                (:name, :mail, :password, :authority)
            ;
            """;

    // mail重複確認用のクエリ
    private static final String FIND_BY_MAIL_SQL = """
            SELECT
                id, name, mail, password, authority
            FROM
                users
            WHERE
                mail = :mail
            ;
            """;

    /**
     * ユーザーの新規作成
     * 
     * @param name     name
     * @param mail     mail
     * @param password password
     */
    public void insertUser(String name, String mail, String password, int authority) {
        logger.debug("Started insertUser");

        SqlParameterSource param = new MapSqlParameterSource().addValue("name", name).addValue("mail", mail)
                .addValue("password", password).addValue("authority", authority);
        template.update(INSERT_SQL, param);
        logger.debug("Finished insertUser");
    }

    /**
     * メールの重複確認
     * 
     * @param mail mail
     * @return 検索結果があればuser, なければnull
     */
    @SuppressWarnings("null") // 警告の抑制
    public User findUserByMail(String mail) {
        logger.debug("Started findUserByMail");

        SqlParameterSource param = new MapSqlParameterSource().addValue("mail", mail);
        List<User> users = template.query(FIND_BY_MAIL_SQL, param, USER_ROW_MAPPER);
        if (users.size() == 0) {
            logger.debug("Finished findUserByMail");
            return null;
        } else {
            User user = users.get(0);

            logger.debug("Finished findUserByMail");
            return user;
        }
    }
}
