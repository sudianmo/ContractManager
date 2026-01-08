package org.example.contractmanager.controller;

import org.example.contractmanager.common.Result;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.ContractFullInfo;
import org.example.contractmanager.service.ContractFullInfoService;
import org.springframework.web.bind.annotation.*;

/**
 * 合同全量信息Controller
 * RESTful API: /api/contracts/full
 * 关键逻辑：提供两个接口 - 单个查询和分页查询
 */
@RestController
@RequestMapping("/api/contracts/full")
@CrossOrigin(originPatterns = "*")
public class ContractFullInfoController {

    private final ContractFullInfoService contractFullInfoService;

    public ContractFullInfoController(ContractFullInfoService contractFullInfoService) {
        this.contractFullInfoService = contractFullInfoService;
    }

    /**
     * 根据ID查询合同完整信息
     * GET /api/contracts/full/{id}
     * @param id 合同ID
     * @return 合同全量信息
     */
    @GetMapping("/{id}")
    public Result<ContractFullInfo> getContractFullInfo(@PathVariable Long id) {
        ContractFullInfo info = contractFullInfoService.getContractFullInfoById(id);
        if (info == null) {
            return Result.error("合同不存在");
        }
        return Result.success(info);
    }

    /**
     * 分页查询合同完整信息
     * GET /api/contracts/full?pageNum=1&pageSize=10
     * @param pageNum 页码（默认1）
     * @param pageSize 每页大小（默认10）
     * @return 分页结果
     */
    @GetMapping
    public Result<PageResultDTO<ContractFullInfo>> getContractFullInfoList(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        
        PageQueryDTO pageQuery = new PageQueryDTO();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        
        PageResultDTO<ContractFullInfo> result = contractFullInfoService.getContractFullInfoByPage(pageQuery);
        return Result.success(result);
    }
}
