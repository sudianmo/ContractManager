package org.example.contractmanager.entity;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 产品实体类
 * 对应数据库Products表
 */
@Data
public class Product {
    
    private Long id;
    private String productName;
    private String specification;
    private BigDecimal unitPrice;
    private Integer stockQuantity;
    private String category;
}
