package org.example.contractmanager.entity;

import lombok.Data;

/**
 * 项目任务进度实体类
 * 对应视图：ProjectTaskProgressView
 * 注意：视图已简化，仅包含核心字段
 */
@Data
public class ProjectTaskProgress {
    
    // 任务信息
    private Long taskId;
    private Long projectId;
    private String projectName;
    private String taskName;
    private String taskAssignee;  // 任务负责人
    private String taskStatus;
    private Integer completionRate;
}
