package org.example.contractmanager.controller;

import org.example.contractmanager.common.Result;
import org.example.contractmanager.dto.ContractDTO;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.service.ContractService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 合同Controller - RESTful API设计
 * 资源路径：/api/contracts
 */
@RestController
@RequestMapping("/api/contracts")
@CrossOrigin(originPatterns = "*")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    /**
     * 创建合同
     * POST /api/contracts
     * @param contractDTO 合同数据
     * @return 创建的合同信息
     */
    @PostMapping
    public Result<ContractDTO> createContract(@RequestBody ContractDTO contractDTO) {
        // TODO: 实现创建接口
        boolean success = contractService.addContract(contractDTO);
        return success ? Result.success("创建成功", contractDTO) : Result.error("创建失败");
    }

    /**
     * 删除合同
     * DELETE /api/contracts/{id}
     * @param id 合同ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteContract(@PathVariable Long id) {
        // TODO: 实现删除接口
        boolean success = contractService.deleteContract(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 更新合同（完整更新）
     * PUT /api/contracts/{id}
     * @param id 合同ID
     * @param contractDTO 合同数据
     * @return 更新后的合同信息
     */
    @PutMapping("/{id}")
    public Result<ContractDTO> updateContract(@PathVariable Long id, @RequestBody ContractDTO contractDTO) {
        // TODO: 实现更新接口
        contractDTO.setId(id);
        boolean success = contractService.updateContract(contractDTO);
        return success ? Result.success("更新成功", contractDTO) : Result.error("更新失败");
    }

    /**
     * 部分更新合同
     * PATCH /api/contracts/{id}
     * @param id 合同ID
     * @param contractDTO 需要更新的字段
     * @return 更新后的合同信息
     */
    @PatchMapping("/{id}")
    public Result<ContractDTO> patchContract(@PathVariable Long id, @RequestBody ContractDTO contractDTO) {
        // TODO: 实现部分更新接口
        contractDTO.setId(id);
        boolean success = contractService.updateContract(contractDTO);
        return success ? Result.success("更新成功", contractDTO) : Result.error("更新失败");
    }

    /**
     * 获取单个合同
     * GET /api/contracts/{id}
     * @param id 合同ID
     * @return 合同详情
     */
    @GetMapping("/{id}")
    public Result<ContractDTO> getContract(@PathVariable Long id) {
        // TODO: 实现查询接口
        ContractDTO contract = contractService.getContractById(id);
        return Result.success(contract);
    }

    /**
     * 获取合同列表（支持分页和筛选）
     * GET /api/contracts?pageNum=1&pageSize=10&keyword=xxx&status=xxx
     * @param pageNum 页码（可选，默认1）
     * @param pageSize 每页大小（可选，默认10）
     * @param keyword 搜索关键字（可选）
     * @param status 合同状态（可选）
     * @param clientId 客户ID（可选）
     * @return 合同列表（分页）
     */
    @GetMapping
    public Result<PageResultDTO<ContractDTO>> getContracts(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long clientId) {
        // TODO: 实现列表查询接口，根据参数动态筛选
        PageQueryDTO pageQuery = new PageQueryDTO();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        pageQuery.setKeyword(keyword);
        pageQuery.setStatus(status);
        pageQuery.setClientId(clientId);
        
        PageResultDTO<ContractDTO> pageResult = contractService.getContractsByPage(pageQuery);
        return Result.success(pageResult);
    }

    /**
     * 获取指定客户的合同列表
     * GET /api/clients/{clientId}/contracts
     * 注意：这个接口更适合放在ClientController中作为子资源
     * @param clientId 客户ID
     * @return 合同列表
     */
    @GetMapping("/client/{clientId}")
    public Result<List<ContractDTO>> getContractsByClient(@PathVariable Long clientId) {
        // TODO: 实现根据客户ID查询接口
        List<ContractDTO> contracts = contractService.getContractsByClientId(clientId);
        return Result.success(contracts);
    }
}
