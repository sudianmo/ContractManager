package org.example.contractmanager.dao.impl;

import org.example.contractmanager.dao.ContractDao;
import org.example.contractmanager.common.BusinessException;
import org.example.contractmanager.entity.Contract;
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
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("dbo")
                .withProcedureName("SP_AddContract")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("ContractNumber", Types.NVARCHAR),
                        new SqlParameter("ContractName", Types.NVARCHAR),
                        new SqlParameter("ProjectID", Types.BIGINT),
                        new SqlParameter("EmployeeID", Types.BIGINT),
                        new SqlParameter("CustomerID", Types.BIGINT),
                        new SqlParameter("Amount", Types.DECIMAL),
                        new SqlParameter("SigningDate", Types.DATE),
                        new SqlParameter("ExpiryDate", Types.DATE),
                        new SqlParameter("Status", Types.VARCHAR),
                        new SqlParameter("Terms", Types.NVARCHAR),
                        new SqlOutParameter("NewContractID", Types.BIGINT),
                        new SqlOutParameter("ResultCode", Types.INTEGER),
                        new SqlOutParameter("ResultMsg", Types.NVARCHAR));

        MapSqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("ContractNumber", contract.getContractNo())
                .addValue("ContractName", contract.getContractName())
                .addValue("ProjectID", contract.getProjectId())
                .addValue("EmployeeID", contract.getEmployeeId())
                .addValue("CustomerID", contract.getClientId())
                .addValue("Amount", contract.getAmount())
                .addValue("SigningDate", contract.getSignDate() != null ? Date.valueOf(contract.getSignDate()) : null)
                .addValue("ExpiryDate", contract.getEndDate() != null ? Date.valueOf(contract.getEndDate()) : null)
                .addValue("Status", contract.getStatus())
                .addValue("Terms", contract.getDescription());

        var out = jdbcCall.execute(inParams);
        Integer resultCode = (Integer) out.get("ResultCode");
        String resultMsg = (String) out.get("ResultMsg");
        if (resultCode == null || resultCode != 1) {
            throw new BusinessException(resultMsg != null ? resultMsg : "创建合同失败");
        }

        Object newIdObj = out.get("NewContractID");
        if (newIdObj instanceof Number) {
            contract.setId(((Number) newIdObj).longValue());
        }

        return 1;
    }

    @Override
    public int deleteById(Long id) {
        return softDelete(id, 1L);
    }

    @Override
    public int update(Contract contract) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("dbo")
                .withProcedureName("SP_UpdateContract")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("ContractID", Types.BIGINT),
                        new SqlParameter("ContractNumber", Types.NVARCHAR),
                        new SqlParameter("ContractName", Types.NVARCHAR),
                        new SqlParameter("ProjectID", Types.BIGINT),
                        new SqlParameter("EmployeeID", Types.BIGINT),
                        new SqlParameter("CustomerID", Types.BIGINT),
                        new SqlParameter("Amount", Types.DECIMAL),
                        new SqlParameter("SigningDate", Types.DATE),
                        new SqlParameter("ExpiryDate", Types.DATE),
                        new SqlParameter("Status", Types.VARCHAR),
                        new SqlParameter("Terms", Types.NVARCHAR),
                        new SqlOutParameter("ResultCode", Types.INTEGER),
                        new SqlOutParameter("ResultMsg", Types.NVARCHAR));

        MapSqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("ContractID", contract.getId())
                .addValue("ContractNumber", contract.getContractNo())
                .addValue("ContractName", contract.getContractName())
                .addValue("ProjectID", contract.getProjectId())
                .addValue("EmployeeID", contract.getEmployeeId())
                .addValue("CustomerID", contract.getClientId())
                .addValue("Amount", contract.getAmount())
                .addValue("SigningDate", contract.getSignDate() != null ? Date.valueOf(contract.getSignDate()) : null)
                .addValue("ExpiryDate", contract.getEndDate() != null ? Date.valueOf(contract.getEndDate()) : null)
                .addValue("Status", contract.getStatus())
                .addValue("Terms", contract.getDescription());

        var out = jdbcCall.execute(inParams);
        Integer resultCode = (Integer) out.get("ResultCode");
        String resultMsg = (String) out.get("ResultMsg");
        if (resultCode == null || resultCode != 1) {
            throw new BusinessException(resultMsg != null ? resultMsg : "更新合同失败");
        }
        return 1;
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
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("dbo")
                .withProcedureName("SP_SoftDeleteContract")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("ContractID", Types.BIGINT),
                        new SqlParameter("OperatorID", Types.BIGINT),
                        new SqlOutParameter("ResultCode", Types.INTEGER),
                        new SqlOutParameter("ResultMsg", Types.NVARCHAR));

        MapSqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("ContractID", id)
                .addValue("OperatorID", operatorId);

        var out = jdbcCall.execute(inParams);
        Integer resultCode = (Integer) out.get("ResultCode");
        String resultMsg = (String) out.get("ResultMsg");
        if (resultCode == null || resultCode != 1) {
            throw new BusinessException(resultMsg != null ? resultMsg : "删除合同失败");
        }
        return 1;
    }
    
    @Override
    public List<Contract> selectDeleted() {
        String sql = "SELECT * FROM Contracts WHERE IsDeleted = 1 ORDER BY DeletedAt DESC";
        return jdbcTemplate.query(sql, contractRowMapper);
    }
    
    @Override
    public int restore(Long id) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("dbo")
                .withProcedureName("SP_RestoreContract")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("ContractID", Types.BIGINT),
                        new SqlOutParameter("ResultCode", Types.INTEGER),
                        new SqlOutParameter("ResultMsg", Types.NVARCHAR));

        MapSqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("ContractID", id);

        var out = jdbcCall.execute(inParams);
        Integer resultCode = (Integer) out.get("ResultCode");
        String resultMsg = (String) out.get("ResultMsg");
        if (resultCode == null || resultCode != 1) {
            throw new BusinessException(resultMsg != null ? resultMsg : "恢复合同失败");
        }
        return 1;
    }
}
