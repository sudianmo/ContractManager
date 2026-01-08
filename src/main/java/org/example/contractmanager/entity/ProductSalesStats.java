package org.example.contractmanager.entity;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 产品销售统计实体类
 * 对应视图：ProductSalesStatsView
 */
@Data
public class ProductSalesStats {
    
    // 产品基本信息
    private Long productId;
    private String productName;
    private String productCategory;
    private BigDecimal standardPrice;
    
    // 销售统计
    private Integer totalSalesQuantity;
    private BigDecimal totalSalesAmount;
    
    // 供应商信息（已废弃：Suppliers表已删除，视图返回NULL）
    @Deprecated
    private String supplierName;
    @Deprecated
    private BigDecimal supplyPrice;
    
    // 毛利
    private BigDecimal grossProfit;
}
