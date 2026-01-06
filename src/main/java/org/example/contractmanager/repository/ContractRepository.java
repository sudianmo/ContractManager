package org.example.contractmanager.repository;

import org.example.contractmanager.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 合同Repository接口
 * 继承JpaRepository，自带基础CRUD方法
 * - save(entity): 新增或更新
 * - deleteById(id): 根据ID删除
 * - findById(id): 根据ID查询
 * - findAll(): 查询所有
 * - count(): 统计总数
 */
@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    
    /**
     * 根据合同编号查询
     * JPA自动根据方法名生成SQL
     */
    Contract findByContractNo(String contractNo);
    
    /**
     * 根据客户ID查询合同列表
     */
    List<Contract> findByClientId(Long clientId);
    
    /**
     * 根据状态查询合同列表
     */
    List<Contract> findByStatus(String status);
    
    /**
     * 根据合同编号或名称模糊查询
     */
    List<Contract> findByContractNoContainingOrContractNameContaining(String contractNo, String contractName);
    
    /**
     * 自定义查询：根据多个条件查询
     * @Query注解可以编写JPQL或原生SQL
     */
    @Query("SELECT c FROM Contract c WHERE " +
           "(:keyword IS NULL OR c.contractNo LIKE %:keyword% OR c.contractName LIKE %:keyword%) " +
           "AND (:status IS NULL OR c.status = :status) " +
           "AND (:clientId IS NULL OR c.clientId = :clientId)")
    List<Contract> searchContracts(@Param("keyword") String keyword,
                                   @Param("status") String status,
                                   @Param("clientId") Long clientId);
}
