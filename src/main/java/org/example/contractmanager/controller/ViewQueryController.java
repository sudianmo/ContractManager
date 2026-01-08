package org.example.contractmanager.controller;

import org.example.contractmanager.common.Result;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 视图查询控制器（人员3任务）
 * 用于查询数据库视图数据
 */
@RestController
@RequestMapping("/api/views")
@CrossOrigin(originPatterns = "*")
public class ViewQueryController {

    private final JdbcTemplate jdbcTemplate;

    public ViewQueryController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 查询客户合同统计视图
     * 需要在SSMS中创建视图：
     * CREATE VIEW V_CustomerContractStats AS
     * SELECT c.CustomerID, c.CustomerName, 
     *        COUNT(ct.ContractID) AS ContractCount,
     *        SUM(ct.Amount) AS TotalAmount
     * FROM Customers c
     * LEFT JOIN Contracts ct ON c.CustomerID = ct.CustomerID
     * GROUP BY c.CustomerID, c.CustomerName
     */
    @GetMapping("/customer-contract-stats")
    public Result<List<Map<String, Object>>> getCustomerContractStats() {
        try {
            String sql = "SELECT * FROM V_CustomerContractStats ORDER BY TotalAmount DESC";
            List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("查询视图失败：" + e.getMessage());
        }
    }

    /**
     * 查询合同详情视图
     * 需要在SSMS中创建视图：
     * CREATE VIEW V_ContractDetails AS
     * SELECT ct.ContractID, ct.ContractNumber, ct.ContractName,
     *        c.CustomerName, e.EmployeeName, p.ProjectName,
     *        ct.Amount, ct.SigningDate, ct.Status
     * FROM Contracts ct
     * LEFT JOIN Customers c ON ct.CustomerID = c.CustomerID
     * LEFT JOIN Employees e ON ct.EmployeeID = e.EmployeeID
     * LEFT JOIN Projects p ON ct.ProjectID = p.ProjectID
     */
    @GetMapping("/contract-details")
    public Result<List<Map<String, Object>>> getContractDetails() {
        try {
            String sql = "SELECT * FROM V_ContractDetails ORDER BY SigningDate DESC";
            List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("查询视图失败：" + e.getMessage());
        }
    }

    /**
     * 查询项目收入统计视图
     * 需要在SSMS中创建视图：
     * CREATE VIEW V_ProjectRevenueStats AS
     * SELECT p.ProjectID, p.ProjectName,
     *        COUNT(ct.ContractID) AS ContractCount,
     *        SUM(ct.Amount) AS TotalRevenue
     * FROM Projects p
     * LEFT JOIN Contracts ct ON p.ProjectID = ct.ProjectID
     * GROUP BY p.ProjectID, p.ProjectName
     */
    @GetMapping("/project-revenue-stats")
    public Result<List<Map<String, Object>>> getProjectRevenueStats() {
        try {
            String sql = "SELECT * FROM V_ProjectRevenueStats ORDER BY TotalRevenue DESC";
            List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("查询视图失败：" + e.getMessage());
        }
    }

    /**
     * 获取数据库中所有视图列表
     */
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> listViews() {
        try {
            String sql = "SELECT TABLE_NAME, VIEW_DEFINITION " +
                        "FROM INFORMATION_SCHEMA.VIEWS " +
                        "WHERE TABLE_SCHEMA = 'dbo'";
            List<Map<String, Object>> views = jdbcTemplate.queryForList(sql);
            return Result.success(views);
        } catch (Exception e) {
            return Result.error("查询视图列表失败：" + e.getMessage());
        }
    }
}
