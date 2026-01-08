package org.example.contractmanager.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 产品数据传输对象
 */
@Data
public class ProductDTO {
    private Long id;
    private String productName;
    private String specification;
    private BigDecimal unitPrice;
    private Integer stockQuantity;
    private String category;
}
