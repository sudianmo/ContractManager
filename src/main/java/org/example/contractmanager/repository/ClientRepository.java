package org.example.contractmanager.repository;

import org.example.contractmanager.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户Repository接口
 * 继承JpaRepository，自带基础CRUD方法
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    /**
     * 根据客户名称查询
     * JPA自动根据方法名生成SQL
     */
    Client findByClientName(String clientName);
    
    /**
     * 根据邮箱查询
     */
    Client findByEmail(String email);
    
    /**
     * 根据客户名称或联系人模糊查询
     */
    List<Client> findByClientNameContainingOrContactPersonContaining(String clientName, String contactPerson);
    
    /**
     * 根据信用等级查询
     */
    List<Client> findByCreditLevel(String creditLevel);
    
    /**
     * 根据公司类型查询
     */
    List<Client> findByCompanyType(String companyType);
}
