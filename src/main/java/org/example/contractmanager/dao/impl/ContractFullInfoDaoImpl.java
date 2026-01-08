package org.example.contractmanager.dao.impl;

import org.example.contractmanager.dao.ContractFullInfoDao;
import org.example.contractmanager.entity.ContractFullInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/**
 * 合同全量信息DAO实现类
 * 使用JdbcTemplate查询视图ContractFullView
 */
@Repository
public class ContractFullInfoDaoImpl implements ContractFullInfoDao {

    private final JdbcTemplate jdbcTemplate;

    public ContractFullInfoDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper - 映射视图查询结果到实体对象
     * 关键逻辑：将ResultSet的每一行映射为ContractFullInfo对象
     */
    private final RowMapper<ContractFullInfo> rowMapper = (rs, rowNum) -> {
        ContractFullInfo info = new ContractFullInfo();
        
        // 合同基本信息
        info.setContractId(rs.getLong("ContractID"));
        info.setContractNumber(rs.getString("ContractNumber"));
        info.setContractName(rs.getString("ContractName"));
        info.setContractAmount(rs.getBigDecimal("ContractAmount"));
        
        // 日期字段需要判空处理
        Date signingDate = rs.getDate("SigningDate");
        if (signingDate != null) {
            info.setSigningDate(signingDate.toLocalDate());
        }
        
        Date expiryDate = rs.getDate("ExpiryDate");
        if (expiryDate != null) {
            info.setExpiryDate(expiryDate.toLocalDate());
        }
        
        info.setContractStatus(rs.getString("ContractStatus"));
        info.setTerms(rs.getString("Terms"));
        info.setTotalPayment(rs.getBigDecimal("TotalPayment"));
        
        // 客户信息（LEFT JOIN可能为NULL）
        info.setCustomerName(rs.getString("CustomerName"));
        info.setCustomerContact(rs.getString("CustomerContact"));
        info.setCustomerPhone(rs.getString("CustomerPhone"));
        
        // 项目信息
        info.setProjectName(rs.getString("ProjectName"));
        info.setProjectStatus(rs.getString("ProjectStatus"));
        
        // 负责人信息
        info.setContractManager(rs.getString("ContractManager"));
        info.setManagerDepartment(rs.getString("ManagerDepartment"));
        
        // 审批信息
        info.setApprovalStatus(rs.getString("ApprovalStatus"));
        
        Date approvalDate = rs.getDate("ApprovalDate");
        if (approvalDate != null) {
            info.setApprovalDate(approvalDate.toLocalDate());
        }
        
        return info;
    };

    @Override
    public ContractFullInfo selectById(Long contractId) {
        // 直接查询视图，无需手动JOIN
        String sql = "SELECT * FROM ContractFullView WHERE ContractID = ?";
        List<ContractFullInfo> results = jdbcTemplate.query(sql, rowMapper, contractId);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<ContractFullInfo> selectAll() {
        String sql = "SELECT * FROM ContractFullView ORDER BY ContractID DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<ContractFullInfo> selectByPage(int offset, int pageSize) {
        // SQL Server分页语法：OFFSET...FETCH NEXT
        String sql = "SELECT * FROM ContractFullView ORDER BY ContractID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return jdbcTemplate.query(sql, rowMapper, offset, pageSize);
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM ContractFullView";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }
}
