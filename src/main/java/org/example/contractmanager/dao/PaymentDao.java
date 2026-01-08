package org.example.contractmanager.dao;

import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.entity.Payment;

import java.math.BigDecimal;
import java.util.List;

/**
 * 支付数据访问接口
 */
public interface PaymentDao {
    
    List<Payment> selectByPage(PageQueryDTO pageQuery);
    
    int countTotal(PageQueryDTO pageQuery);
    
    Payment selectById(Long id);
    
    int insert(Payment payment);
    
    int update(Payment payment);
    
    int deleteById(Long id);
    
    List<Payment> selectByContractId(Long contractId);
    
    int countByContractId(Long contractId);
    
    BigDecimal sumTotalPayments();
    
    int softDelete(Long id, Long operatorId);
    List<Payment> selectDeleted();
    int restore(Long id);
}
