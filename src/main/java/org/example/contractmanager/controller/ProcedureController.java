package org.example.contractmanager.controller;

import org.example.contractmanager.common.Result;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.web.bind.annotation.*;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存储过程调用控制器（人员4任务）
 * 用于调用数据库存储过程
 */
@RestController
@RequestMapping("/api/procedures")
@CrossOrigin(origins = "*")
public class ProcedureController {

    private final JdbcTemplate jdbcTemplate;

    public ProcedureController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 调用存储过程 - 客户合同统计
     * 需要在SSMS中创建存储过程：
     * CREATE PROCEDURE SP_GetCustomerContractStats
     *     @CustomerID BIGINT
     * AS
     * BEGIN
     *     SELECT COUNT(*) AS ContractCount, 
     *            SUM(Amount) AS TotalAmount,
     *            AVG(Amount) AS AvgAmount
     *     FROM Contracts
     *     WHERE CustomerID = @CustomerID
     * END
     */
    @GetMapping("/customer-stats/{customerId}")
    public Result<Map<String, Object>> getCustomerStats(@PathVariable Long customerId) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withProcedureName("SP_GetCustomerContractStats");
            
            Map<String, Object> inParams = new HashMap<>();
            inParams.put("CustomerID", customerId);
            
            Map<String, Object> result = jdbcCall.execute(inParams);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("调用存储过程失败：" + e.getMessage());
        }
    }

    /**
     * 调用存储过程 - 批量更新合同状态
     * 需要在SSMS中创建存储过程：
     * CREATE PROCEDURE SP_BatchUpdateContractStatus
     *     @OldStatus VARCHAR(20),
     *     @NewStatus VARCHAR(20)
     * AS
     * BEGIN
     *     UPDATE Contracts SET Status = @NewStatus WHERE Status = @OldStatus
     *     SELECT @@ROWCOUNT AS UpdatedRows
     * END
     */
    @PostMapping("/batch-update-status")
    public Result<Map<String, Object>> batchUpdateContractStatus(
            @RequestParam String oldStatus, 
            @RequestParam String newStatus) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withProcedureName("SP_BatchUpdateContractStatus");
            
            Map<String, Object> inParams = new HashMap<>();
            inParams.put("OldStatus", oldStatus);
            inParams.put("NewStatus", newStatus);
            
            Map<String, Object> result = jdbcCall.execute(inParams);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("批量更新失败：" + e.getMessage());
        }
    }

    /**
     * 调用存储过程 - 计算项目收入
     * 需要在SSMS中创建存储过程：
     * CREATE PROCEDURE SP_CalculateProjectRevenue
     *     @ProjectID BIGINT,
     *     @TotalRevenue DECIMAL(18,2) OUTPUT
     * AS
     * BEGIN
     *     SELECT @TotalRevenue = SUM(Amount)
     *     FROM Contracts
     *     WHERE ProjectID = @ProjectID
     * END
     */
    @GetMapping("/project-revenue/{projectId}")
    public Result<Map<String, Object>> calculateProjectRevenue(@PathVariable Long projectId) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withProcedureName("SP_CalculateProjectRevenue")
                    .declareParameters(
                        new SqlParameter("ProjectID", Types.BIGINT),
                        new SqlParameter("TotalRevenue", Types.DECIMAL)
                    );
            
            Map<String, Object> inParams = new HashMap<>();
            inParams.put("ProjectID", projectId);
            
            Map<String, Object> result = jdbcCall.execute(inParams);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("计算项目收入失败：" + e.getMessage());
        }
    }

    /**
     * 调用存储过程 - 复杂报表查询
     * 需要在SSMS中创建存储过程：
     * CREATE PROCEDURE SP_GetContractReport
     *     @StartDate DATE,
     *     @EndDate DATE
     * AS
     * BEGIN
     *     SELECT c.CustomerName, ct.ContractNumber, ct.Amount, ct.SigningDate
     *     FROM Contracts ct
     *     JOIN Customers c ON ct.CustomerID = c.CustomerID
     *     WHERE ct.SigningDate BETWEEN @StartDate AND @EndDate
     *     ORDER BY ct.SigningDate DESC
     * END
     */
    @GetMapping("/contract-report")
    public Result<List<Map<String, Object>>> getContractReport(
            @RequestParam String startDate, 
            @RequestParam String endDate) {
        try {
            String sql = "EXEC SP_GetContractReport @StartDate=?, @EndDate=?";
            List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, startDate, endDate);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("查询报表失败：" + e.getMessage());
        }
    }

    /**
     * 获取数据库中所有存储过程列表
     */
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> listProcedures() {
        try {
            String sql = "SELECT name, object_id, create_date, modify_date " +
                        "FROM sys.procedures " +
                        "WHERE schema_id = SCHEMA_ID('dbo')";
            List<Map<String, Object>> procedures = jdbcTemplate.queryForList(sql);
            return Result.success(procedures);
        } catch (Exception e) {
            return Result.error("查询存储过程列表失败：" + e.getMessage());
        }
    }
}
