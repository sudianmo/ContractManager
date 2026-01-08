package org.example.contractmanager.controller;

import org.example.contractmanager.common.Result;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 触发器测试控制器（人员3任务）
 * 用于测试和验证数据库触发器功能
 */
@RestController
@RequestMapping("/api/triggers")
@CrossOrigin(originPatterns = "*")
public class TriggerTestController {

    private final JdbcTemplate jdbcTemplate;

    public TriggerTestController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 测试触发器 - 示例：客户状态变更触发器
     * 在SSMS中创建触发器后，通过此接口验证触发器是否生效
     */
    @PostMapping("/test-customer-status")
    public Result<Map<String, Object>> testCustomerStatusTrigger(@RequestParam Long customerId, 
                                                                   @RequestParam String newStatus) {
        try {
            // 更新客户状态，触发器会自动记录变更历史
            String sql = "UPDATE Customers SET Status = ? WHERE CustomerID = ?";
            int rows = jdbcTemplate.update(sql, newStatus, customerId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("updatedRows", rows);
            result.put("message", "触发器已执行，请检查审计表");
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("触发器测试失败：" + e.getMessage());
        }
    }

    /**
     * 测试触发器 - 示例：合同金额自动计算触发器
     * 需要在SSMS中创建对应触发器
     */
    @PostMapping("/test-contract-amount")
    public Result<Map<String, Object>> testContractAmountTrigger(@RequestParam Long contractId) {
        try {
            // 查询合同总金额（触发器应该自动计算TotalPayment）
            String sql = "SELECT Amount, TotalPayment FROM Contracts WHERE ContractID = ?";
            Map<String, Object> result = jdbcTemplate.queryForMap(sql, contractId);
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("触发器测试失败：" + e.getMessage());
        }
    }

    /**
     * 获取触发器列表
     * 查询数据库中已创建的触发器
     */
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> listTriggers() {
        try {
            String sql = "SELECT name, object_id, type_desc, create_date, modify_date " +
                        "FROM sys.triggers WHERE parent_class = 1";
            List<Map<String, Object>> triggers = jdbcTemplate.queryForList(sql);
            
            return Result.success(triggers);
        } catch (Exception e) {
            return Result.error("查询触发器失败：" + e.getMessage());
        }
    }
}
