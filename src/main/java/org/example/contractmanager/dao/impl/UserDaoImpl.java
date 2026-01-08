package org.example.contractmanager.dao.impl;

import org.example.contractmanager.dao.UserDao;
import org.example.contractmanager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用户DAO实现类
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> rowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getInt("UserID"));
            user.setUsername(rs.getString("Username"));
            user.setPassword(rs.getString("Password"));
            user.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
            return user;
        }
    };

    @Override
    public User selectByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE Username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
