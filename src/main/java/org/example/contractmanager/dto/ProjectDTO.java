package org.example.contractmanager.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 项目数据传输对象
 */
@Data
public class ProjectDTO {
    private Long id;
    private String projectName;
    private Long customerId;
    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal budget;
    private String status;
    private String description;
}
