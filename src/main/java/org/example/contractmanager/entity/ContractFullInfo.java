package org.example.contractmanager.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 合同全量信息实体类
 * 对应视图：ContractFullView
 */
@Data
public class ContractFullInfo {
    
    // 合同基本信息
    private Long contractId;
    private String contractNumber;
    private String contractName;
    private BigDecimal contractAmount;
    private LocalDate signingDate;
    private LocalDate expiryDate;
    private String contractStatus;
    private String terms;
    private BigDecimal totalPayment;
    
    // 客户信息
    private String customerName;
    private String customerContact;
    private String customerPhone;
    
    // 项目信息
    private String projectName;
    private String projectStatus;
    
    // 负责人信息
    private String contractManager;
    private String managerDepartment;
    
    // 审批信息（已废弃：Approvals表已删除，视图返回NULL）
    @Deprecated
    private String approvalStatus;
    @Deprecated
    private LocalDate approvalDate;
}
