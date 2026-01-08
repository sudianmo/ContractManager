package org.example.contractmanager.dao.impl;

import org.example.contractmanager.dao.ContractDao;
import org.example.contractmanager.entity.Contract;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * 合同Dao实现类（人员2任务）
 * 使用JdbcTemplate操作SQL Server数据库
 */
@Repository
public class ContractDaoImpl implements ContractDao {

    private final JdbcTemplate jdbcTemplate;

    public ContractDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper - 将ResultSet映射为Contract对象
     */
    private final RowMapper<Contract> contractRowMapper = (rs, rowNum) -> {
        Contract contract = new Contract();
        contract.setId(rs.getLong("ContractID"));
        contract.setContractNo(rs.getString("ContractNumber"));
        contract.setContractName(rs.getString("ContractName")); // 允许NULL
        contract.setProjectId(rs.getLong("ProjectID"));
        contract.setEmployeeId(rs.getLong("EmployeeID"));
        
        // CustomerID允许NULL
        Long customerId = rs.getObject("CustomerID", Long.class);
        contract.setClientId(customerId);
        
        contract.setAmount(rs.getBigDecimal("Amount"));
        
        Date signDate = rs.getDate("SigningDate");
        if (signDate != null) contract.setSignDate(signDate.toLocalDate());
        
        Date endDate = rs.getDate("ExpiryDate");
        if (endDate != null) contract.setEndDate(endDate.toLocalDate());
        
        contract.setStatus(rs.getString("Status"));
        contract.setDescription(rs.getString("Terms"));
        contract.setTotalPayment(rs.getBigDecimal("TotalPayment"));
        
        return contract;
    };

    @Override
    public int insert(Contract contract) {
        String sql = "INSERT INTO Contracts (ContractNumber, ContractName, ProjectID, EmployeeID, CustomerID, " +
                     "Amount, SigningDate, ExpiryDate, Status, Terms, TotalPayment) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, contract.getContractNo());
            ps.setString(2, contract.getContractName());
            ps.setLong(3, contract.getProjectId());
            ps.setLong(4, contract.getEmployeeId());
            
            if (contract.getClientId() != null) {
                ps.setLong(5, contract.getClientId());
            } else {
                ps.setNull(5, java.sql.Types.BIGINT);
            }
            
            ps.setBigDecimal(6, contract.getAmount());
            ps.setDate(7, contract.getSignDate() != null ? Date.valueOf(contract.getSignDate()) : null);
            ps.setDate(8, contract.getEndDate() != null ? Date.valueOf(contract.getEndDate()) : null);
            ps.setString(9, contract.getStatus() != null ? contract.getStatus() : "Signed");
            ps.setString(10, contract.getDescription());
            ps.setBigDecimal(11, contract.getTotalPayment() != null ? contract.getTotalPayment() : new java.math.BigDecimal("0.00"));
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : 0;
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM Contracts WHERE ContractID = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public int update(Contract contract) {
        String sql = "UPDATE Contracts SET ContractNumber=?, ContractName=?, ProjectID=?, EmployeeID=?, " +
                     "CustomerID=?, Amount=?, SigningDate=?, ExpiryDate=?, Status=?, Terms=? " +
                     "WHERE ContractID=?";
        return jdbcTemplate.update(sql,
            contract.getContractNo(),
            contract.getContractName(),
            contract.getProjectId(),
            contract.getEmployeeId(),
            contract.getClientId(),
            contract.getAmount(),
            contract.getSignDate() != null ? Date.valueOf(contract.getSignDate()) : null,
            contract.getEndDate() != null ? Date.valueOf(contract.getEndDate()) : null,
            contract.getStatus(),
            contract.getDescription(),
            contract.getId()
        );
    }

    @Override
    public Contract selectById(Long id) {
        String sql = "SELECT * FROM Contracts WHERE ContractID = ? AND IsDeleted = 0";
        List<Contract> results = jdbcTemplate.query(sql, contractRowMapper, id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Contract> selectAll() {
        String sql = "SELECT * FROM Contracts WHERE IsDeleted = 0 ORDER BY ContractID";
        return jdbcTemplate.query(sql, contractRowMapper);
    }

    @Override
    public List<Contract> selectByPage(int offset, int pageSize) {
        String sql = "SELECT * FROM Contracts WHERE IsDeleted = 0 ORDER BY ContractID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return jdbcTemplate.query(sql, contractRowMapper, offset, pageSize);
    }

    @Override
    public List<Contract> selectByClientId(Long clientId) {
        String sql = "SELECT * FROM Contracts WHERE CustomerID = ? AND IsDeleted = 0";
        return jdbcTemplate.query(sql, contractRowMapper, clientId);
    }

    @Override
    public List<Contract> selectByStatus(String status) {
        String sql = "SELECT * FROM Contracts WHERE Status = ? AND IsDeleted = 0";
        return jdbcTemplate.query(sql, contractRowMapper, status);
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM Contracts WHERE IsDeleted = 0";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    @Override
    public List<Contract> searchByKeyword(String keyword) {
        String sql = "SELECT * FROM Contracts WHERE (ContractNumber LIKE ? OR ContractName LIKE ?) AND IsDeleted = 0";
        String searchPattern = "%" + keyword + "%";
        return jdbcTemplate.query(sql, contractRowMapper, searchPattern, searchPattern);
    }
    
    @Override
    public int countByProjectId(Long projectId) {
        String sql = "SELECT COUNT(*) FROM Contracts WHERE ProjectID = ? AND IsDeleted = 0";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, projectId);
        return count != null ? count : 0;
    }
    
    @Override
    public int softDelete(Long id, Long operatorId) {
        String sql = "UPDATE Contracts SET IsDeleted = 1, DeletedBy = ?, DeletedAt = GETDATE() WHERE ContractID = ?";
        return jdbcTemplate.update(sql, operatorId, id);
    }
    
    @Override
    public List<Contract> selectDeleted() {
        String sql = "SELECT * FROM Contracts WHERE IsDeleted = 1 ORDER BY DeletedAt DESC";
        return jdbcTemplate.query(sql, contractRowMapper);
    }
    
    @Override
    public int restore(Long id) {
        String sql = "UPDATE Contracts SET IsDeleted = 0, DeletedBy = NULL, DeletedAt = NULL WHERE ContractID = ?";
        return jdbcTemplate.update(sql, id);
    }
}
