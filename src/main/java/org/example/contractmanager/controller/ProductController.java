package org.example.contractmanager.controller;

import org.example.contractmanager.common.Result;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.dto.ProductDTO;
import org.example.contractmanager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(originPatterns = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public Result<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        boolean success = productService.createProduct(productDTO);
        return success ? Result.success("创建成功", productDTO) : Result.error("创建失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        boolean success = productService.deleteProduct(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    @PutMapping("/{id}")
    public Result<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        productDTO.setId(id);
        boolean success = productService.updateProduct(productDTO);
        return success ? Result.success("更新成功", productDTO) : Result.error("更新失败");
    }

    @GetMapping("/{id}")
    public Result<ProductDTO> getProduct(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return Result.success(product);
    }

    @GetMapping
    public Result<PageResultDTO<ProductDTO>> getProducts(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        PageQueryDTO pageQuery = new PageQueryDTO();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        pageQuery.setKeyword(keyword);
        
        PageResultDTO<ProductDTO> pageResult = productService.getProductsByPage(pageQuery);
        return Result.success(pageResult);
    }

    @GetMapping("/low-stock")
    public Result<List<ProductDTO>> getLowStockProducts(
            @RequestParam(required = false, defaultValue = "10") Integer threshold) {
        List<ProductDTO> products = productService.getLowStockProducts(threshold);
        return Result.success(products);
    }
}
