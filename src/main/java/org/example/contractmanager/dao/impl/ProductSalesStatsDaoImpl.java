package org.example.contractmanager.dao.impl;

import org.example.contractmanager.dao.ProductSalesStatsDao;
import org.example.contractmanager.entity.ProductSalesStats;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 产品销售统计DAO实现类
 * 使用JdbcTemplate查询视图ProductSalesStatsView
 */
@Repository
public class ProductSalesStatsDaoImpl implements ProductSalesStatsDao {

    private final JdbcTemplate jdbcTemplate;

    public ProductSalesStatsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper - 映射视图查询结果到实体对象
     * 关键逻辑：将聚合统计结果映射为ProductSalesStats对象
     */
    private final RowMapper<ProductSalesStats> rowMapper = (rs, rowNum) -> {
        ProductSalesStats stats = new ProductSalesStats();
        
        // 产品基本信息
        stats.setProductId(rs.getLong("ProductID"));
        stats.setProductName(rs.getString("ProductName"));
        stats.setProductCategory(rs.getString("ProductCategory"));
        stats.setStandardPrice(rs.getBigDecimal("StandardPrice"));
        
        // 销售统计
        stats.setTotalSalesQuantity(rs.getInt("TotalSalesQuantity"));
        stats.setTotalSalesAmount(rs.getBigDecimal("TotalSalesAmount"));
        
        // 供应商信息
        stats.setSupplierName(rs.getString("SupplierName"));
        stats.setSupplyPrice(rs.getBigDecimal("SupplyPrice"));
        
        // 毛利（销售金额-供货成本）
        stats.setGrossProfit(rs.getBigDecimal("GrossProfit"));
        
        return stats;
    };

    @Override
    public ProductSalesStats selectByProductId(Long productId) {
        // 查询指定产品的销售统计
        String sql = "SELECT * FROM ProductSalesStatsView WHERE ProductID = ?";
        List<ProductSalesStats> results = jdbcTemplate.query(sql, rowMapper, productId);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<ProductSalesStats> selectAll() {
        // 按销售金额降序排列
        String sql = "SELECT * FROM ProductSalesStatsView ORDER BY TotalSalesAmount DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<ProductSalesStats> selectByPage(int offset, int pageSize) {
        // SQL Server分页语法：按销售金额降序
        String sql = "SELECT * FROM ProductSalesStatsView ORDER BY TotalSalesAmount DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return jdbcTemplate.query(sql, rowMapper, offset, pageSize);
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM ProductSalesStatsView";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }
}
