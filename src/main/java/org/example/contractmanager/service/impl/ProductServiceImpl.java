package org.example.contractmanager.service.impl;

import org.example.contractmanager.common.BusinessException;
import org.example.contractmanager.dao.ProductDao;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.dto.ProductDTO;
import org.example.contractmanager.entity.Product;
import org.example.contractmanager.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public PageResultDTO<ProductDTO> getProductsByPage(PageQueryDTO pageQuery) {
        List<Product> products = productDao.selectByPage(pageQuery);
        int total = productDao.countTotal(pageQuery);
        
        List<ProductDTO> records = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new PageResultDTO<>((long) total, records);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productDao.selectById(id);
        return product != null ? convertToDTO(product) : null;
    }

    @Override
    public boolean createProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        return productDao.insert(product) > 0;
    }

    @Override
    public boolean updateProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        return productDao.update(product) > 0;
    }

    @Override
    public boolean deleteProduct(Long id) {
        // 检查是否有合同使用该产品
        int usageCount = productDao.countContractProductsByProductId(id);
        if (usageCount > 0) {
            throw new BusinessException("该产品被 " + usageCount + " 个合同使用，无法删除");
        }
        
        return productDao.softDelete(id, 1L) > 0;
    }

    @Override
    public List<ProductDTO> getLowStockProducts(Integer threshold) {
        return productDao.selectLowStock(threshold).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        BeanUtils.copyProperties(product, dto);
        return dto;
    }

    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        BeanUtils.copyProperties(dto, product);
        return product;
    }
}
