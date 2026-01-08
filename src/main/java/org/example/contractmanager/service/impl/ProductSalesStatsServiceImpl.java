package org.example.contractmanager.service.impl;

import org.example.contractmanager.dao.ProductSalesStatsDao;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.ProductSalesStats;
import org.example.contractmanager.service.ProductSalesStatsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品销售统计Service实现类
 * 业务逻辑层：调用DAO查询视图数据
 */
@Service
public class ProductSalesStatsServiceImpl implements ProductSalesStatsService {

    private final ProductSalesStatsDao productSalesStatsDao;

    public ProductSalesStatsServiceImpl(ProductSalesStatsDao productSalesStatsDao) {
        this.productSalesStatsDao = productSalesStatsDao;
    }

    @Override
    public ProductSalesStats getSalesStatsByProductId(Long productId) {
        // 查询指定产品的销售统计
        return productSalesStatsDao.selectByProductId(productId);
    }

    @Override
    public PageResultDTO<ProductSalesStats> getSalesStatsByPage(PageQueryDTO pageQuery) {
        // 计算偏移量：(页码-1) * 每页大小
        int offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        List<ProductSalesStats> records = productSalesStatsDao.selectByPage(offset, pageQuery.getPageSize());
        long total = productSalesStatsDao.count();
        return new PageResultDTO<>(total, records);
    }
}
