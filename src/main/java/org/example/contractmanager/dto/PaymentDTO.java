package org.example.contractmanager.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 支付数据传输对象
 */
@Data
public class PaymentDTO {
    private Long id;
    private Long contractId;
    private String contractName;
    private BigDecimal paymentAmount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String paymentStatus;
    private String remarks;
}
