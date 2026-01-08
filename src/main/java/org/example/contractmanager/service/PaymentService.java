package org.example.contractmanager.service;

import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.dto.PaymentDTO;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    
    PageResultDTO<PaymentDTO> getPaymentsByPage(PageQueryDTO pageQuery);
    
    PaymentDTO getPaymentById(Long id);
    
    boolean createPayment(PaymentDTO paymentDTO);
    
    boolean updatePayment(PaymentDTO paymentDTO);
    
    boolean deletePayment(Long id);
    
    List<PaymentDTO> getPaymentsByContractId(Long contractId);
    
    BigDecimal getTotalPayments();
}
