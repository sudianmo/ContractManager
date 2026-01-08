package org.example.contractmanager.dao.impl;

import org.example.contractmanager.dao.ClientDao;
import org.example.contractmanager.common.BusinessException;
import org.example.contractmanager.entity.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Types;
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
    private final RowMapper<Customer> clientRowMapper = (rs, rowNum) -> {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getLong("CustomerID"));
        customer.setCustomerName(rs.getString("CustomerName"));
        customer.setContactPerson(rs.getString("ContactPerson"));
        customer.setPhone(rs.getString("Phone"));
        customer.setEmail(rs.getString("Email"));
        customer.setAddress(rs.getString("Address"));

        Date regDate = rs.getDate("RegistrationDate");
        if (regDate != null) {
            customer.setRegistrationDate(regDate.toLocalDate());
        }

        customer.setStatus(rs.getString("Status"));
        return customer;
    };

    @Override
    public int insert(Customer customer) {
        String email = customer.getEmail();
        if (email != null && email.trim().isEmpty()) {
            email = null;
        }

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("dbo")
                .withProcedureName("SP_AddCustomer")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("CustomerName", Types.NVARCHAR),
                        new SqlParameter("ContactPerson", Types.NVARCHAR),
                        new SqlParameter("Phone", Types.NVARCHAR),
                        new SqlParameter("Email", Types.NVARCHAR),
                        new SqlParameter("Address", Types.NVARCHAR),
                        new SqlParameter("RegistrationDate", Types.DATE),
                        new SqlParameter("Status", Types.VARCHAR),
                        new SqlOutParameter("NewCustomerID", Types.BIGINT),
                        new SqlOutParameter("ResultCode", Types.INTEGER),
                        new SqlOutParameter("ResultMsg", Types.NVARCHAR));

        MapSqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("CustomerName", customer.getCustomerName())
                .addValue("ContactPerson", customer.getContactPerson())
                .addValue("Phone", customer.getPhone())
                .addValue("Email", email)
                .addValue("Address", customer.getAddress())
                .addValue("RegistrationDate",
                        customer.getRegistrationDate() != null ? Date.valueOf(customer.getRegistrationDate()) : null)
                .addValue("Status", customer.getStatus());

        var out = jdbcCall.execute(inParams);
        Integer resultCode = (Integer) out.get("ResultCode");
        String resultMsg = (String) out.get("ResultMsg");

        if (resultCode == null || resultCode != 1) {
            throw new BusinessException(resultMsg != null ? resultMsg : "新增客户失败");
        }

        Object newIdObj = out.get("NewCustomerID");
        if (newIdObj instanceof Number) {
            customer.setCustomerId(((Number) newIdObj).longValue());
        }

        return 1;
    }

    @Override
    public int deleteById(Long id) {
        // 保留物理删除方法，仅供管理员使用
        String sql = "DELETE FROM Customers WHERE CustomerID = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public int softDelete(Long id, Long operatorId) {
        String sql = "UPDATE Customers SET IsDeleted = 1, DeletedBy = ?, DeletedAt = GETDATE() WHERE CustomerID = ?";
        return jdbcTemplate.update(sql, operatorId, id);
    }

    @Override
    public int update(Customer customer) {
        String sql = "UPDATE Customers SET CustomerName=?, ContactPerson=?, Phone=?, Email=?, Address=?, Status=? " +
                "WHERE CustomerID=?";
        return jdbcTemplate.update(sql,
                customer.getCustomerName(),
                customer.getContactPerson(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getStatus(),
                customer.getCustomerId());
    }

    @Override
    public Customer selectById(Long id) {
        String sql = "SELECT * FROM Customers WHERE CustomerID = ? AND IsDeleted = 0";
        List<Customer> results = jdbcTemplate.query(sql, clientRowMapper, id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Customer> selectAll() {
        String sql = "SELECT * FROM Customers WHERE IsDeleted = 0 ORDER BY CustomerID";
        return jdbcTemplate.query(sql, clientRowMapper);
    }

    @Override
    public List<Customer> selectByPage(int offset, int pageSize) {
        String sql = "SELECT * FROM Customers WHERE IsDeleted = 0 ORDER BY CustomerID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return jdbcTemplate.query(sql, clientRowMapper, offset, pageSize);
    }

    @Override
    public Customer selectByName(String clientName) {
        String sql = "SELECT * FROM Customers WHERE CustomerName = ? AND IsDeleted = 0";
        List<Customer> results = jdbcTemplate.query(sql, clientRowMapper, clientName);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM Customers WHERE IsDeleted = 0";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    @Override
    public List<Customer> searchByKeyword(String keyword) {
        String sql = "SELECT * FROM Customers WHERE (CustomerName LIKE ? OR ContactPerson LIKE ?) AND IsDeleted = 0";
        String searchPattern = "%" + keyword + "%";
        return jdbcTemplate.query(sql, clientRowMapper, searchPattern, searchPattern);
    }

    @Override
    public List<Customer> selectDeleted() {
        String sql = "SELECT * FROM Customers WHERE IsDeleted = 1 ORDER BY DeletedAt DESC";
        return jdbcTemplate.query(sql, clientRowMapper);
    }

    @Override
    public int restore(Long id) {
        String sql = "UPDATE Customers SET IsDeleted = 0, DeletedBy = NULL, DeletedAt = NULL WHERE CustomerID = ?";
        return jdbcTemplate.update(sql, id);
    }
}
