package org.example.contractmanager.dao;

import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.entity.Product;

import java.util.List;

/**
 * 产品数据访问接口
 */
public interface ProductDao {
    
    List<Product> selectByPage(PageQueryDTO pageQuery);
    
    int countTotal(PageQueryDTO pageQuery);
    
    Product selectById(Long id);
    
    int insert(Product product);
    
    int update(Product product);
    
    int deleteById(Long id);
    
    List<Product> selectLowStock(Integer threshold);
    
    /**
     * 检查产品是否被合同使用
     * @param productId 产品ID
     * @return 使用该产品的合同数量
     */
    int countContractProductsByProductId(Long productId);
    
    int softDelete(Long id, Long operatorId);
    List<Product> selectDeleted();
    int restore(Long id);
}
