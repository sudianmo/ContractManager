package org.example.contractmanager.dao;

import org.example.contractmanager.entity.Customer;
import java.util.List;

/**
 * 客户DAO接口
 * 定义客户表的数据访问方法
 */
public interface ClientDao {

    /**
     * 新增客户
     * @param client 客户对象
     * @return 影响的行数
     */
    int insert(Customer customer);

    /**
     * 根据ID删除客户
     * @param id 客户ID
     * @return 影响的行数
     */
    int deleteById(Long id);

    /**
     * 更新客户信息
     * @param client 客户对象
     * @return 影响的行数
     */
    int update(Customer customer);

    /**
     * 根据ID查询客户
     * @param id 客户ID
     * @return 客户对象
     */
    Customer selectById(Long id);

    /**
     * 查询所有客户
     * @return 客户列表
     */
    List<Customer> selectAll();

    /**
     * 分页查询客户
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 客户列表
     */
    List<Customer> selectByPage(int offset, int pageSize);

    /**
     * 根据客户名称查询
     * @param clientName 客户名称
     * @return 客户对象
     */
    Customer selectByName(String clientName);

    /**
     * 查询客户总数
     * @return 总数
     */
    long count();

    /**
     * 根据关键字搜索客户（客户名称或联系人）
     * @param keyword 关键字
     * @return 客户列表
     */
    List<Customer> searchByKeyword(String keyword);
    
    /**
     * 软删除客户
     * @param id 客户ID
     * @param operatorId 操作员ID
     * @return 影响的行数
     */
    int softDelete(Long id, Long operatorId);
    
    /**
     * 查询已删除的客户（管理员）
     * @return 已删除客户列表
     */
    List<Customer> selectDeleted();
    
    /**
     * 恢复已删除的客户（管理员）
     * @param id 客户ID
     * @return 影响的行数
     */
    int restore(Long id);
}
