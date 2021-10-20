package com.example.onlinestatus.repository;

import com.example.onlinestatus.exception.AuthException;
import com.example.onlinestatus.exception.BadRequestException;
import com.example.onlinestatus.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_REGISTER =
            "INSERT INTO users(user_id, name, date_of_birth) VALUES (NEXTVAL('users_seq'), ?, ?)";

    private static final String SQL_REGISTER_WITH_ACTIVE =
            "INSERT INTO users(user_id, name, date_of_birth, active) VALUES (NEXTVAL('users_seq'), ?, ?, ?)";

    private static final String SQL_GET_ALL_ACTIVE_USERS =
            "SELECT * FROM users WHERE active = ?";

    private static final String SQL_ACTIVATE_USER =
            "UPDATE users SET active = ? WHERE user_id = ?";

    private static final String SQL_USER_STATUS =
            "SELECT active FROM users WHERE user_id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM users WHERE user_id = ?";

    private static final String SQL_FIND_USER =
            "SELECT COUNT(*) FROM users WHERE user_id = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer create(String name, Date date_of_birth) {

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {

                PreparedStatement preparedStatement = connection.prepareStatement(SQL_REGISTER, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, name);
                preparedStatement.setDate(2, date_of_birth);

                return preparedStatement;
            }, keyHolder);

            return (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("user_id");
        } catch (Exception e) {
            throw new AuthException("Invalid details, " + e.getMessage());
        }

    }

    @Override
    public Integer createWithActive(String name, Date date_of_birth, Boolean active) {

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {

                PreparedStatement preparedStatement = connection.prepareStatement(SQL_REGISTER_WITH_ACTIVE, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, name);
                preparedStatement.setDate(2, date_of_birth);
                preparedStatement.setBoolean(3, active);

                return preparedStatement;
            }, keyHolder);

            return (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("user_id");
        } catch (Exception e) {
            throw new AuthException("Invalid details, " + e.getMessage());
        }

    }

    @Override
    public List<User> getActiveUsers(Boolean active) {
        return jdbcTemplate.query(SQL_GET_ALL_ACTIVE_USERS, new Object[]{active}, userMapper);
    }

    @Override
    public Integer findUser(Integer id) {
        return jdbcTemplate.queryForObject(SQL_FIND_USER, new Object[]{id}, Integer.class);
    }

    @Override
    public Boolean setActive(Integer id, Boolean active) {
        try {
            jdbcTemplate.update(SQL_ACTIVATE_USER, active, id);
            return jdbcTemplate.query(SQL_USER_STATUS, new Object[]{id}, statusMapper).get(0);
        } catch (Exception e) {
            throw new BadRequestException("Bad request, " + e.getMessage());
        }
    }

    @Override
    public Boolean deleteUser(Integer id) {
        try {
            jdbcTemplate.update(SQL_DELETE, id);
            return true;
        } catch (Exception e) {
            throw new BadRequestException("Bad request, " + e.getMessage());
        }
    }

    private final RowMapper<User> userMapper = (((rs, rowNum) -> new User(
            rs.getInt("user_id"),
            rs.getString("name"),
            rs.getDate("date_of_birth"),
            rs.getBoolean("active")
    )));

    private final RowMapper<Boolean> statusMapper = (((rs, rowNum) -> rs.getBoolean("active")));
}
