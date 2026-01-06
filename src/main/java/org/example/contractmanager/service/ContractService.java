package org.example.contractmanager.service;

import org.example.contractmanager.dto.ContractDTO;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.Contract;

import java.util.List;

/**
 * 合同Service接口
 * 定义合同相关的业务逻辑方法
 */
public interface ContractService {

    /**
     * 新增合同
     * @param contractDTO 合同DTO
     * @return 是否成功
     */
    boolean addContract(ContractDTO contractDTO);

    /**
     * 删除合同
     * @param id 合同ID
     * @return 是否成功
     */
    boolean deleteContract(Long id);

    /**
     * 更新合同
     * @param contractDTO 合同DTO
     * @return 是否成功
     */
    boolean updateContract(ContractDTO contractDTO);

    /**
     * 根据ID查询合同
     * @param id 合同ID
     * @return 合同DTO
     */
    ContractDTO getContractById(Long id);

    /**
     * 查询所有合同
     * @return 合同DTO列表
     */
    List<ContractDTO> getAllContracts();

    /**
     * 分页查询合同
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    PageResultDTO<ContractDTO> getContractsByPage(PageQueryDTO pageQuery);

    /**
     * 根据客户ID查询合同
     * @param clientId 客户ID
     * @return 合同DTO列表
     */
    List<ContractDTO> getContractsByClientId(Long clientId);

    /**
     * 根据状态查询合同
     * @param status 合同状态
     * @return 合同DTO列表
     */
    List<ContractDTO> getContractsByStatus(String status);

    /**
     * 搜索合同
     * @param keyword 关键字
     * @return 合同DTO列表
     */
    List<ContractDTO> searchContracts(String keyword);
}
