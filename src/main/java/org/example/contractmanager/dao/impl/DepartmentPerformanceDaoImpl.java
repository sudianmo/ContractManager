package org.example.contractmanager.dao.impl;

import org.example.contractmanager.dao.DepartmentPerformanceDao;
import org.example.contractmanager.entity.DepartmentPerformance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部门业绩统计DAO实现类
 * 使用JdbcTemplate查询视图DepartmentPerformanceView
 */
@Repository
public class DepartmentPerformanceDaoImpl implements DepartmentPerformanceDao {

    private final JdbcTemplate jdbcTemplate;

    public DepartmentPerformanceDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper - 映射视图查询结果到实体对象
     * 关键逻辑：将部门业绩聚合统计结果映射为DepartmentPerformance对象
     */
    private final RowMapper<DepartmentPerformance> rowMapper = (rs, rowNum) -> {
        DepartmentPerformance performance = new DepartmentPerformance();
        
        // 部门信息
        performance.setDepartment(rs.getString("Department"));
        
        // 业绩统计
        performance.setContractCount(rs.getInt("ContractCount"));
        performance.setTotalContractAmount(rs.getBigDecimal("TotalContractAmount"));
        performance.setTotalPaidAmount(rs.getBigDecimal("TotalPaidAmount"));
        
        // 年度信息
        performance.setContractYear(rs.getInt("ContractYear"));
        
        // Top负责人
        performance.setTopManager(rs.getString("TopManager"));
        
        return performance;
    };

    @Override
    public List<DepartmentPerformance> selectByDepartment(String department) {
        // 查询指定部门的业绩统计（按年度降序）
        String sql = "SELECT * FROM DepartmentPerformanceView WHERE Department = ? ORDER BY ContractYear DESC";
        return jdbcTemplate.query(sql, rowMapper, department);
    }

    @Override
    public List<DepartmentPerformance> selectByYear(Integer year) {
        // 查询指定年度的部门业绩统计（按合同金额降序）
        String sql = "SELECT * FROM DepartmentPerformanceView WHERE ContractYear = ? ORDER BY TotalContractAmount DESC";
        return jdbcTemplate.query(sql, rowMapper, year);
    }

    @Override
    public List<DepartmentPerformance> selectAll() {
        // 按年度降序、合同金额降序排列
        String sql = "SELECT * FROM DepartmentPerformanceView ORDER BY ContractYear DESC, TotalContractAmount DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<DepartmentPerformance> selectByPage(int offset, int pageSize) {
        // SQL Server分页语法：按年度降序、合同金额降序
        String sql = "SELECT * FROM DepartmentPerformanceView ORDER BY ContractYear DESC, TotalContractAmount DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return jdbcTemplate.query(sql, rowMapper, offset, pageSize);
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM DepartmentPerformanceView";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }
}
