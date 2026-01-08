package org.example.contractmanager.controller;

import org.example.contractmanager.common.Result;
import org.example.contractmanager.dto.PageQueryDTO;
import org.example.contractmanager.dto.PageResultDTO;
import org.example.contractmanager.dto.PaymentDTO;
import org.example.contractmanager.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(originPatterns = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public Result<PaymentDTO> createPayment(@RequestBody PaymentDTO paymentDTO) {
        boolean success = paymentService.createPayment(paymentDTO);
        return success ? Result.success("创建成功", paymentDTO) : Result.error("创建失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deletePayment(@PathVariable Long id) {
        boolean success = paymentService.deletePayment(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    @PutMapping("/{id}")
    public Result<PaymentDTO> updatePayment(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        paymentDTO.setId(id);
        boolean success = paymentService.updatePayment(paymentDTO);
        return success ? Result.success("更新成功", paymentDTO) : Result.error("更新失败");
    }

    @GetMapping("/{id}")
    public Result<PaymentDTO> getPayment(@PathVariable Long id) {
        PaymentDTO payment = paymentService.getPaymentById(id);
        return Result.success(payment);
    }

    @GetMapping
    public Result<PageResultDTO<PaymentDTO>> getPayments(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long contractId) {
        PageQueryDTO pageQuery = new PageQueryDTO();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        pageQuery.setContractId(contractId);
        
        PageResultDTO<PaymentDTO> pageResult = paymentService.getPaymentsByPage(pageQuery);
        return Result.success(pageResult);
    }

    @GetMapping("/contract/{contractId}")
    public Result<List<PaymentDTO>> getPaymentsByContract(@PathVariable Long contractId) {
        List<PaymentDTO> payments = paymentService.getPaymentsByContractId(contractId);
        return Result.success(payments);
    }

    @GetMapping("/total")
    public Result<BigDecimal> getTotalPayments() {
        BigDecimal total = paymentService.getTotalPayments();
        return Result.success(total);
    }
}
