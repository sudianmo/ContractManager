package org.example.contractmanager.dao;

import org.example.contractmanager.entity.ContractFullInfo;
import java.util.List;

/**
 * 合同全量信息DAO接口
 * 查询ContractFullView视图
 */
public interface ContractFullInfoDao {

    /**
     * 根据合同ID查询完整信息
     * @param contractId 合同ID
     * @return 合同全量信息
     */
    ContractFullInfo selectById(Long contractId);

    /**
     * 查询所有合同完整信息
     * @return 合同全量信息列表
     */
    List<ContractFullInfo> selectAll();

    /**
     * 分页查询合同完整信息
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 合同全量信息列表
     */
    List<ContractFullInfo> selectByPage(int offset, int pageSize);

    /**
     * 统计合同总数
     * @return 总数
     */
    long count();
}
