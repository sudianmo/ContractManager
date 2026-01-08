package org.example.contractmanager.dao;

import org.example.contractmanager.entity.Contract;
import java.util.List;

/**
 * 合同DAO接口
 * 定义合同表的数据访问方法
 */
public interface ContractDao {

    /**
     * 新增合同
     * @param contract 合同对象
     * @return 影响的行数
     */
    int insert(Contract contract);

    /**
     * 根据ID删除合同
     * @param id 合同ID
     * @return 影响的行数
     */
    int deleteById(Long id);

    /**
     * 更新合同信息
     * @param contract 合同对象
     * @return 影响的行数
     */
    int update(Contract contract);

    /**
     * 根据ID查询合同
     * @param id 合同ID
     * @return 合同对象
     */
    Contract selectById(Long id);

    /**
     * 查询所有合同
     * @return 合同列表
     */
    List<Contract> selectAll();

    /**
     * 分页查询合同
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 合同列表
     */
    List<Contract> selectByPage(int offset, int pageSize);

    /**
     * 根据客户ID查询合同
     * @param clientId 客户ID
     * @return 合同列表
     */
    List<Contract> selectByClientId(Long clientId);

    /**
     * 根据合同状态查询
     * @param status 合同状态
     * @return 合同列表
     */
    List<Contract> selectByStatus(String status);

    /**
     * 查询合同总数
     * @return 总数
     */
    long count();

    /**
     * 根据关键字搜索合同（合同编号或名称）
     * @param keyword 关键字
     * @return 合同列表
     */
    List<Contract> searchByKeyword(String keyword);
    
    /**
     * 统计项目下的合同数量
     * @param projectId 项目ID
     * @return 合同数量
     */
    int countByProjectId(Long projectId);
    
    int softDelete(Long id, Long operatorId);
    List<Contract> selectDeleted();
    int restore(Long id);
}
