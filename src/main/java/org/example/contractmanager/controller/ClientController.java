package org.example.contractmanager.controller;

import org.example.contractmanager.common.Result;
import org.example.contractmanager.dto.ClientDTO;
import org.example.contractmanager.dto.ContractDTO;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.service.ClientService;
import org.example.contractmanager.service.ContractService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户Controller - RESTful API设计
 * 资源路径：/api/clients
 */
@RestController
@RequestMapping("/api/clients")
@CrossOrigin(originPatterns = "*")
public class ClientController {

    private final ClientService clientService;
    private final ContractService contractService;

    public ClientController(ClientService clientService, ContractService contractService) {
        this.clientService = clientService;
        this.contractService = contractService;
    }

    /**
     * 创建客户
     * POST /api/clients
     * @param clientDTO 客户数据
     * @return 创建的客户信息
     */
    @PostMapping
    public Result<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        // TODO: 实现创建接口
        boolean success = clientService.addClient(clientDTO);
        return success ? Result.success("创建成功", clientDTO) : Result.error("创建失败");
    }

    /**
     * 删除客户
     * DELETE /api/clients/{id}
     * @param id 客户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteClient(@PathVariable Long id) {
        // TODO: 实现删除接口
        boolean success = clientService.deleteClient(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 更新客户（完整更新）
     * PUT /api/clients/{id}
     * @param id 客户ID
     * @param clientDTO 客户数据
     * @return 更新后的客户信息
     */
    @PutMapping("/{id}")
    public Result<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        // TODO: 实现更新接口
        clientDTO.setId(id);
        boolean success = clientService.updateClient(clientDTO);
        return success ? Result.success("更新成功", clientDTO) : Result.error("更新失败");
    }

    /**
     * 部分更新客户
     * PATCH /api/clients/{id}
     * @param id 客户ID
     * @param clientDTO 需要更新的字段
     * @return 更新后的客户信息
     */
    @PatchMapping("/{id}")
    public Result<ClientDTO> patchClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        // TODO: 实现部分更新接口
        clientDTO.setId(id);
        boolean success = clientService.updateClient(clientDTO);
        return success ? Result.success("更新成功", clientDTO) : Result.error("更新失败");
    }

    /**
     * 获取单个客户
     * GET /api/clients/{id}
     * @param id 客户ID
     * @return 客户详情
     */
    @GetMapping("/{id}")
    public Result<ClientDTO> getClient(@PathVariable Long id) {
        // TODO: 实现查询接口
        ClientDTO client = clientService.getClientById(id);
        return Result.success(client);
    }

    /**
     * 获取客户列表（支持分页和筛选）
     * GET /api/clients?pageNum=1&pageSize=10&keyword=xxx
     * @param pageNum 页码（可选，默认1）
     * @param pageSize 每页大小（可选，默认10）
     * @param keyword 搜索关键字（可选）
     * @return 客户列表（分页）
     */
    @GetMapping
    public Result<PageResultDTO<ClientDTO>> getClients(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        // TODO: 实现列表查询接口，根据参数动态筛选
        PageQueryDTO pageQuery = new PageQueryDTO();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        pageQuery.setKeyword(keyword);
        
        PageResultDTO<ClientDTO> pageResult = clientService.getClientsByPage(pageQuery);
        return Result.success(pageResult);
    }

    /**
     * 获取指定客户的所有合同（子资源）
     * GET /api/clients/{id}/contracts
     * @param id 客户ID
     * @return 该客户的合同列表
     */
    @GetMapping("/{id}/contracts")
    public Result<List<ContractDTO>> getClientContracts(@PathVariable Long id) {
        // TODO: 实现获取客户合同列表接口
        List<ContractDTO> contracts = contractService.getContractsByClientId(id);
        return Result.success(contracts);
    }
}
