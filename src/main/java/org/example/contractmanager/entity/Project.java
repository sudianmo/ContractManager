package org.example.contractmanager.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 项目实体类
 * 对应数据库Projects表
 */
@Data
public class Project {
    
    private Long id;
    private String projectName;
    private Long customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal budget;
    private String status;
    private String description;
}
