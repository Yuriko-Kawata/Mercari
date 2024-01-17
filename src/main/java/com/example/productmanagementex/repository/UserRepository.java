package com.example.productmanagementex.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.productmanagementex.domain.User;

@Repository
public class UserRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, i) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setMail(rs.getString("mail"));
        user.setPassword(rs.getString("password"));
        user.setAuthority(rs.getInt("authority"));
        return user;
    };

    private static final String INSERT_SQL = """
            INSERT INTO
                users(name, mail, password)
            VALUES
                (:name, :mail, :password)
            ;
            """;

    private static final String FIND_BY_MAIL_SQL = """
            SELECT
                id, name, mail, password, authority
            FROM
                users
            WHERE
                mail = :mail
            ;
            """;

    public void insertUser(String name, String mail, String password) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", name).addValue("mail", mail)
                .addValue("password", password);
        template.update(INSERT_SQL, param);
    }

    public User findUserByMail(String mail) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("mail", mail);
        User user = template.queryForObject(FIND_BY_MAIL_SQL, param, USER_ROW_MAPPER);
        return user;
    }
}
