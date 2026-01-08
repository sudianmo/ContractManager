package org.example.contractmanager.entity;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 部门业绩统计实体类
 * 对应视图：DepartmentPerformanceView
 */
@Data
public class DepartmentPerformance {
    
    // 部门信息
    private String department;
    
    // 业绩统计
    private Integer contractCount;
    private BigDecimal totalContractAmount;
    private BigDecimal totalPaidAmount;
    
    // 年度信息
    private Integer contractYear;
    
    // Top负责人
    private String topManager;
}
