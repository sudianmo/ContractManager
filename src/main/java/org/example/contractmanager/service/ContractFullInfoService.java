package org.example.contractmanager.service;

import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.ContractFullInfo;

/**
 * 合同全量信息Service接口
 */
public interface ContractFullInfoService {

    /**
     * 根据ID查询合同完整信息
     * @param contractId 合同ID
     * @return 合同全量信息
     */
    ContractFullInfo getContractFullInfoById(Long contractId);

    /**
     * 分页查询合同完整信息
     * @param pageQuery 分页参数
     * @return 分页结果
     */
    PageResultDTO<ContractFullInfo> getContractFullInfoByPage(PageQueryDTO pageQuery);
}
