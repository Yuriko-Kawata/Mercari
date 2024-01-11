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
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setAuthority(rs.getInt("authority"));
        return user;
    };

    private static final String INSERT_SQL = """
            INSERT INTO
                users(name, email, password)
            VALUES
                (:name, :email, :password)
            ;
            """;

    public void insertUser(String name, String email, String password) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", name).addValue("email", email)
                .addValue("password", password);
        template.update(INSERT_SQL, param);
    }

}
