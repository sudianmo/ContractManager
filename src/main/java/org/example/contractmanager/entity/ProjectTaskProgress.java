package org.example.contractmanager.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 项目任务进度实体类
 * 对应视图：ProjectTaskProgressView
 */
@Data
public class ProjectTaskProgress {
    
    // 项目基本信息
    private Long projectId;
    private String projectName;
    private Long customerId;
    private String customerName;
    private BigDecimal budget;
    private String projectStatus;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    
    // 任务信息
    private Long taskId;
    private String taskName;
    private String taskDescription;
    private LocalDate taskStartDate;
    private LocalDate taskEndDate;
    private String taskStatus;
    private Integer completionRate;
    
    // 任务分配信息
    private String taskAssignee;
    private String assigneeDepartment;
    
    // 关联合同信息
    private String contractNumber;
    private String contractName;
    private BigDecimal contractAmount;
}
