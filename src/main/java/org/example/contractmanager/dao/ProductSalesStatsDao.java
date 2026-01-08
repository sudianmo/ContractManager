package org.example.contractmanager.dao;

import org.example.contractmanager.entity.ProductSalesStats;
import java.util.List;

/**
 * 产品销售统计DAO接口
 * 查询ProductSalesStatsView视图
 */
public interface ProductSalesStatsDao {

    /**
     * 根据产品ID查询销售统计
     * @param productId 产品ID
     * @return 销售统计
     */
    ProductSalesStats selectByProductId(Long productId);

    /**
     * 查询所有产品销售统计
     * @return 销售统计列表
     */
    List<ProductSalesStats> selectAll();

    /**
     * 分页查询产品销售统计
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 销售统计列表
     */
    List<ProductSalesStats> selectByPage(int offset, int pageSize);

    /**
     * 统计总数
     * @return 总数
     */
    long count();
}
