package org.example.contractmanager.service;

import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.ProductSalesStats;

/**
 * 产品销售统计Service接口
 */
public interface ProductSalesStatsService {

    /**
     * 根据产品ID查询销售统计
     * @param productId 产品ID
     * @return 销售统计
     */
    ProductSalesStats getSalesStatsByProductId(Long productId);

    /**
     * 分页查询产品销售统计
     * @param pageQuery 分页参数
     * @return 分页结果
     */
    PageResultDTO<ProductSalesStats> getSalesStatsByPage(PageQueryDTO pageQuery);
}
