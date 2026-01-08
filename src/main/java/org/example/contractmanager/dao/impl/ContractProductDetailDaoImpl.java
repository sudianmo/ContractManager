package org.example.contractmanager.dao.impl;

import org.example.contractmanager.dao.ContractProductDetailDao;
import org.example.contractmanager.entity.ContractProductDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 合同产品明细DAO实现类
 * 使用JdbcTemplate查询视图ContractProductDetailView
 */
@Repository
public class ContractProductDetailDaoImpl implements ContractProductDetailDao {

    private final JdbcTemplate jdbcTemplate;

    public ContractProductDetailDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper - 映射视图查询结果到实体对象
     * 关键逻辑：将ResultSet的每一行映射为ContractProductDetail对象
     */
    private final RowMapper<ContractProductDetail> rowMapper = (rs, rowNum) -> {
        ContractProductDetail detail = new ContractProductDetail();
        
        // 合同信息
        detail.setContractId(rs.getLong("ContractID"));
        detail.setContractNumber(rs.getString("ContractNumber"));
        detail.setContractName(rs.getString("ContractName"));
        detail.setContractStatus(rs.getString("ContractStatus"));
        
        // 客户信息
        detail.setCustomerName(rs.getString("CustomerName"));
        
        // 产品信息
        detail.setProductId(rs.getLong("ProductID"));
        detail.setProductName(rs.getString("ProductName"));
        detail.setSpecification(rs.getString("Specification"));
        detail.setProductCategory(rs.getString("ProductCategory"));
        
        // 产品明细
        detail.setQuantity(rs.getInt("Quantity"));
        detail.setUnitPrice(rs.getBigDecimal("UnitPrice"));
        detail.setSubtotal(rs.getBigDecimal("Subtotal"));
        
        return detail;
    };

    @Override
    public List<ContractProductDetail> selectByContractId(Long contractId) {
        // 根据合同ID查询该合同的所有产品明细
        String sql = "SELECT * FROM ContractProductDetailView WHERE ContractID = ? ORDER BY ProductID";
        return jdbcTemplate.query(sql, rowMapper, contractId);
    }

    @Override
    public List<ContractProductDetail> selectAll() {
        String sql = "SELECT * FROM ContractProductDetailView ORDER BY ContractID DESC, ProductID";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<ContractProductDetail> selectByPage(int offset, int pageSize) {
        // SQL Server分页语法：OFFSET...FETCH NEXT
        String sql = "SELECT * FROM ContractProductDetailView ORDER BY ContractID DESC, ProductID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return jdbcTemplate.query(sql, rowMapper, offset, pageSize);
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM ContractProductDetailView";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }
}
