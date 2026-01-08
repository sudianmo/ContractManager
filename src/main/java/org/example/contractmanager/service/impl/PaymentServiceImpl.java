package org.example.contractmanager.service.impl;

import org.example.contractmanager.dao.PaymentDao;
import org.example.contractmanager.dao.ContractDao;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.dto.PaymentDTO;
import org.example.contractmanager.entity.Payment;
import org.example.contractmanager.entity.Contract;
import org.example.contractmanager.service.PaymentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    @Autowired
    private ContractDao contractDao;

    @Override
    public PageResultDTO<PaymentDTO> getPaymentsByPage(PageQueryDTO pageQuery) {
        List<Payment> payments = paymentDao.selectByPage(pageQuery);
        int total = paymentDao.countTotal(pageQuery);
        
        List<PaymentDTO> records = payments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new PageResultDTO<>((long) total, records);
    }

    @Override
    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentDao.selectById(id);
        return payment != null ? convertToDTO(payment) : null;
    }

    @Override
    public boolean createPayment(PaymentDTO paymentDTO) {
        Payment payment = convertToEntity(paymentDTO);
        return paymentDao.insert(payment) > 0;
    }

    @Override
    public boolean updatePayment(PaymentDTO paymentDTO) {
        Payment payment = convertToEntity(paymentDTO);
        return paymentDao.update(payment) > 0;
    }

    @Override
    public boolean deletePayment(Long id) {
        return paymentDao.deleteById(id) > 0;
    }

    @Override
    public List<PaymentDTO> getPaymentsByContractId(Long contractId) {
        return paymentDao.selectByContractId(contractId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalPayments() {
        return paymentDao.sumTotalPayments();
    }

    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        BeanUtils.copyProperties(payment, dto);
        
        if (payment.getContractId() != null) {
            Contract contract = contractDao.selectById(payment.getContractId());
            if (contract != null) {
                dto.setContractName(contract.getContractName());
            }
        }
        
        return dto;
    }

    private Payment convertToEntity(PaymentDTO dto) {
        Payment payment = new Payment();
        BeanUtils.copyProperties(dto, payment);
        return payment;
    }
}
