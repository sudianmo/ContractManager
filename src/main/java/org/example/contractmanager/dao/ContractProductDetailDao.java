package org.example.contractmanager.dao;

import org.example.contractmanager.entity.ContractProductDetail;
import java.util.List;

/**
 * 合同产品明细DAO接口
 * 查询ContractProductDetailView视图
 */
public interface ContractProductDetailDao {

    /**
     * 根据合同ID查询产品明细
     * @param contractId 合同ID
     * @return 产品明细列表
     */
    List<ContractProductDetail> selectByContractId(Long contractId);

    /**
     * 查询所有合同产品明细
     * @return 产品明细列表
     */
    List<ContractProductDetail> selectAll();

    /**
     * 分页查询合同产品明细
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 产品明细列表
     */
    List<ContractProductDetail> selectByPage(int offset, int pageSize);

    /**
     * 统计总数
     * @return 总数
     */
    long count();
}
