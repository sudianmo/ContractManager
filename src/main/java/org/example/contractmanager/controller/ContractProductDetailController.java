package org.example.contractmanager.controller;

import org.example.contractmanager.common.Result;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.ContractProductDetail;
import org.example.contractmanager.service.ContractProductDetailService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 合同产品明细Controller
 * RESTful API: /api/contracts/products
 * 关键逻辑：提供两个接口 - 按合同查询和分页查询
 */
@RestController
@RequestMapping("/api/contracts/products")
@CrossOrigin(originPatterns = "*")
public class ContractProductDetailController {

    private final ContractProductDetailService contractProductDetailService;

    public ContractProductDetailController(ContractProductDetailService contractProductDetailService) {
        this.contractProductDetailService = contractProductDetailService;
    }

    /**
     * 根据合同ID查询产品明细
     * GET /api/contracts/products/contract/{contractId}
     * @param contractId 合同ID
     * @return 产品明细列表
     */
    @GetMapping("/contract/{contractId}")
    public Result<List<ContractProductDetail>> getProductDetailsByContract(@PathVariable Long contractId) {
        List<ContractProductDetail> details = contractProductDetailService.getProductDetailsByContractId(contractId);
        return Result.success(details);
    }

    /**
     * 分页查询合同产品明细
     * GET /api/contracts/products?pageNum=1&pageSize=10
     * @param pageNum 页码（默认1）
     * @param pageSize 每页大小（默认10）
     * @return 分页结果
     */
    @GetMapping
    public Result<PageResultDTO<ContractProductDetail>> getProductDetailsList(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        
        PageQueryDTO pageQuery = new PageQueryDTO();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        
        PageResultDTO<ContractProductDetail> result = contractProductDetailService.getProductDetailsByPage(pageQuery);
        return Result.success(result);
    }
}
