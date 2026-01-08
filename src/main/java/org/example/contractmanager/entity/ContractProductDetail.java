package org.example.contractmanager.entity;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 合同产品明细实体类
 * 对应视图：ContractProductDetailView
 */
@Data
public class ContractProductDetail {
    
    // 合同信息
    private Long contractId;
    private String contractNumber;
    private String contractName;
    private String contractStatus;
    
    // 客户信息
    private String customerName;
    
    // 产品信息
    private Long productId;
    private String productName;
    private String specification;
    private String productCategory;
    
    // 产品明细
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
