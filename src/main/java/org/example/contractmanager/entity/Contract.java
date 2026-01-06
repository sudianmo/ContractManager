package org.example.contractmanager.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 合同实体类（纯POJO，不使用JPA）
 * 对应数据库Contracts表
 */
@Data
public class Contract {
    
    private Long id;
    private String contractNo;
    private String contractName;
    private Long projectId;
    private Long employeeId;
    private Long clientId;
    private BigDecimal amount;
    private LocalDate signDate;
    private LocalDate endDate;
    private String status;
    private String description;
    private BigDecimal totalPayment;
    

}
