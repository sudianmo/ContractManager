package org.example.contractmanager.service;

import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.entity.ContractProductDetail;

import java.util.List;

/**
 * 合同产品明细Service接口
 */
public interface ContractProductDetailService {

    /**
     * 根据合同ID查询产品明细
     * @param contractId 合同ID
     * @return 产品明细列表
     */
    List<ContractProductDetail> getProductDetailsByContractId(Long contractId);

    /**
     * 分页查询合同产品明细
     * @param pageQuery 分页参数
     * @return 分页结果
     */
    PageResultDTO<ContractProductDetail> getProductDetailsByPage(PageQueryDTO pageQuery);
}
