package org.example.contractmanager.dao.impl;

import org.example.contractmanager.dao.ClientDao;
import org.example.contractmanager.entity.Client;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 客户Dao实现类（人员1任务）
 * 使用JdbcTemplate操作SQL Server数据库
 */
@Repository
public class ClientDaoImpl implements ClientDao {

    private final JdbcTemplate jdbcTemplate;

    public ClientDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper - 将ResultSet映射为Client对象
     */
    private final RowMapper<Client> clientRowMapper = (rs, rowNum) -> {
        Client client = new Client();
        client.setId(rs.getLong("CustomerID"));
        client.setClientName(rs.getString("CustomerName"));
        client.setContactPerson(rs.getString("ContactPerson"));
        client.setPhone(rs.getString("Phone"));
        client.setEmail(rs.getString("Email"));
        client.setAddress(rs.getString("Address"));
        
        Date regDate = rs.getDate("RegistrationDate");
        if (regDate != null) {
            client.setRegistrationDate(regDate.toLocalDate());
        }
        
        client.setStatus(rs.getString("Status"));
        return client;
    };

    @Override
    public int insert(Client client) {
        String sql = "INSERT INTO Customers (CustomerName, ContactPerson, Phone, Email, Address, RegistrationDate, Status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, client.getClientName());
            ps.setString(2, client.getContactPerson());
            ps.setString(3, client.getPhone());
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getAddress());
            ps.setDate(6, client.getRegistrationDate() != null ? Date.valueOf(client.getRegistrationDate()) : Date.valueOf(java.time.LocalDate.now()));
            ps.setString(7, client.getStatus() != null ? client.getStatus() : "Active");
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : 0;
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM Customers WHERE CustomerID = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public int update(Client client) {
        String sql = "UPDATE Customers SET CustomerName=?, ContactPerson=?, Phone=?, Email=?, Address=?, Status=? " +
                     "WHERE CustomerID=?";
        return jdbcTemplate.update(sql, 
            client.getClientName(),
            client.getContactPerson(),
            client.getPhone(),
            client.getEmail(),
            client.getAddress(),
            client.getStatus(),
            client.getId()
        );
    }

    @Override
    public Client selectById(Long id) {
        String sql = "SELECT * FROM Customers WHERE CustomerID = ?";
        List<Client> results = jdbcTemplate.query(sql, clientRowMapper, id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Client> selectAll() {
        String sql = "SELECT * FROM Customers ORDER BY CustomerID";
        return jdbcTemplate.query(sql, clientRowMapper);
    }

    @Override
    public List<Client> selectByPage(int offset, int pageSize) {
        String sql = "SELECT * FROM Customers ORDER BY CustomerID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return jdbcTemplate.query(sql, clientRowMapper, offset, pageSize);
    }

    @Override
    public Client selectByName(String clientName) {
        String sql = "SELECT * FROM Customers WHERE CustomerName = ?";
        List<Client> results = jdbcTemplate.query(sql, clientRowMapper, clientName);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM Customers";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    @Override
    public List<Client> searchByKeyword(String keyword) {
        String sql = "SELECT * FROM Customers WHERE CustomerName LIKE ? OR ContactPerson LIKE ?";
        String searchPattern = "%" + keyword + "%";
        return jdbcTemplate.query(sql, clientRowMapper, searchPattern, searchPattern);
    }
}
