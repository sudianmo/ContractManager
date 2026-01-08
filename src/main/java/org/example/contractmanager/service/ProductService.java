package org.example.contractmanager.service;

import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    
    PageResultDTO<ProductDTO> getProductsByPage(PageQueryDTO pageQuery);
    
    ProductDTO getProductById(Long id);
    
    boolean createProduct(ProductDTO productDTO);
    
    boolean updateProduct(ProductDTO productDTO);
    
    boolean deleteProduct(Long id);
    
    List<ProductDTO> getLowStockProducts(Integer threshold);
}
