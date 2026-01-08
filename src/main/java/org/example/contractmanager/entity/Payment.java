package org.example.contractmanager.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 支付实体类
 * 对应数据库Payments表
 */
@Data
public class Payment {
    
    private Long id;
    private Long contractId;
    private BigDecimal paymentAmount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String paymentStatus;
    private String remarks;
}
