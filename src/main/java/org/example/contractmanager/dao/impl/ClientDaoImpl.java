package org.example.contractmanager.dao.impl;

import org.example.contractmanager.dao.ClientDao;
import org.example.contractmanager.entity.Client;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 客户DAO实现类
 * 使用JdbcTemplate操作数据库
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
    private final RowMapper<Client> clientRowMapper = new RowMapper<Client>() {
        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
            Client client = new Client();
            // TODO: 根据实际数据库表字段完成映射
            // client.setId(rs.getLong("id"));
            // client.setClientName(rs.getString("client_name"));
            // ...其他字段映射
            return client;
        }
    };

    @Override
    public int insert(Client client) {
        // TODO: 实现插入逻辑
        String sql = "INSERT INTO client (...) VALUES (...)";
        return 0;
    }

    @Override
    public int deleteById(Long id) {
        // TODO: 实现删除逻辑
        String sql = "DELETE FROM client WHERE id = ?";
        return 0;
    }

    @Override
    public int update(Client client) {
        // TODO: 实现更新逻辑
        String sql = "UPDATE client SET ... WHERE id = ?";
        return 0;
    }

    @Override
    public Client selectById(Long id) {
        // TODO: 实现根据ID查询逻辑
        String sql = "SELECT * FROM client WHERE id = ?";
        return null;
    }

    @Override
    public List<Client> selectAll() {
        // TODO: 实现查询所有逻辑
        String sql = "SELECT * FROM client";
        return null;
    }

    @Override
    public List<Client> selectByPage(int offset, int pageSize) {
        // TODO: 实现分页查询逻辑（SQL Server使用OFFSET FETCH）
        String sql = "SELECT * FROM client ORDER BY id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return null;
    }

    @Override
    public Client selectByName(String clientName) {
        // TODO: 实现根据名称查询逻辑
        String sql = "SELECT * FROM client WHERE client_name = ?";
        return null;
    }

    @Override
    public long count() {
        // TODO: 实现查询总数逻辑
        String sql = "SELECT COUNT(*) FROM client";
        return 0;
    }

    @Override
    public List<Client> searchByKeyword(String keyword) {
        // TODO: 实现关键字搜索逻辑
        String sql = "SELECT * FROM client WHERE client_name LIKE ? OR contact_person LIKE ?";
        return null;
    }
}
