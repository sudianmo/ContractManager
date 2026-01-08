package org.example.contractmanager.controller;

import org.example.contractmanager.common.Result;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.ProductSalesStats;
import org.example.contractmanager.service.ProductSalesStatsService;
import org.springframework.web.bind.annotation.*;

/**
 * 产品销售统计Controller
 * RESTful API: /api/products/sales-stats
 * 关键逻辑：提供两个接口 - 按产品查询和分页查询
 */
@RestController
@RequestMapping("/api/products/sales-stats")
@CrossOrigin(originPatterns = "*")
public class ProductSalesStatsController {

    private final ProductSalesStatsService productSalesStatsService;

    public ProductSalesStatsController(ProductSalesStatsService productSalesStatsService) {
        this.productSalesStatsService = productSalesStatsService;
    }

    /**
     * 根据产品ID查询销售统计
     * GET /api/products/sales-stats/{productId}
     * @param productId 产品ID
     * @return 销售统计
     */
    @GetMapping("/{productId}")
    public Result<ProductSalesStats> getSalesStatsByProduct(@PathVariable Long productId) {
        ProductSalesStats stats = productSalesStatsService.getSalesStatsByProductId(productId);
        if (stats == null) {
            return Result.error("产品销售统计不存在");
        }
        return Result.success(stats);
    }

    /**
     * 分页查询产品销售统计
     * GET /api/products/sales-stats?pageNum=1&pageSize=10
     * @param pageNum 页码（默认1）
     * @param pageSize 每页大小（默认10）
     * @return 分页结果
     */
    @GetMapping
    public Result<PageResultDTO<ProductSalesStats>> getSalesStatsList(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        
        PageQueryDTO pageQuery = new PageQueryDTO();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        
        PageResultDTO<ProductSalesStats> result = productSalesStatsService.getSalesStatsByPage(pageQuery);
        return Result.success(result);
    }
}
