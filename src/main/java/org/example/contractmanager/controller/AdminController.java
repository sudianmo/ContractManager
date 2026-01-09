package org.example.contractmanager.controller;

import org.example.contractmanager.common.Result;
import org.example.contractmanager.dao.*;
import org.example.contractmanager.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员Controller - 数据回收站管理
 * 提供查看已删除数据、恢复数据、永久删除数据的功能
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private PaymentDao paymentDao;

    // ==================== 客户回收站 ====================

    /**
     * 查看已删除的客户列表
     */
    @GetMapping("/clients/deleted")
    public Result<List<Customer>> getDeletedClients() {
        List<Customer> deletedClients = clientDao.selectDeleted();
        return Result.success(deletedClients);
    }

    /**
     * 恢复已删除的客户
     */
    @PutMapping("/clients/restore/{id}")
    public Result<Void> restoreClient(@PathVariable Long id) {
        int result = clientDao.restore(id);
        if (result > 0) {
            return Result.success();
        }
        return Result.error("恢复客户失败");
    }

    /**
     * 永久删除客户（物理删除）
     * 注意：此操作不可逆
     */
    @DeleteMapping("/clients/permanent/{id}")
    public Result<Void> permanentDeleteClient(@PathVariable Long id) {
        try {
            int result = clientDao.deleteById(id);
            if (result > 0) {
                return Result.success();
            }
            return Result.error("永久删除客户失败");
        } catch (Exception e) {
            return Result.error("存在关联数据，无法永久删除");
        }
    }

    // ==================== 产品回收站 ====================

    /**
     * 查看已删除的产品列表
     */
    @GetMapping("/products/deleted")
    public Result<List<Product>> getDeletedProducts() {
        List<Product> deletedProducts = productDao.selectDeleted();
        return Result.success(deletedProducts);
    }

    /**
     * 恢复已删除的产品
     */
    @PutMapping("/products/restore/{id}")
    public Result<Void> restoreProduct(@PathVariable Long id) {
        int result = productDao.restore(id);
        if (result > 0) {
            return Result.success();
        }
        return Result.error("恢复产品失败");
    }

    /**
     * 永久删除产品（物理删除）
     */
    @DeleteMapping("/products/permanent/{id}")
    public Result<Void> permanentDeleteProduct(@PathVariable Long id) {
        try {
            int result = productDao.deleteById(id);
            if (result > 0) {
                return Result.success();
            }
            return Result.error("永久删除产品失败");
        } catch (Exception e) {
            return Result.error("存在关联数据，无法永久删除");
        }
    }

    // ==================== 项目回收站 ====================

    /**
     * 查看已删除的项目列表
     */
    @GetMapping("/projects/deleted")
    public Result<List<Project>> getDeletedProjects() {
        List<Project> deletedProjects = projectDao.selectDeleted();
        return Result.success(deletedProjects);
    }

    /**
     * 恢复已删除的项目
     */
    @PutMapping("/projects/restore/{id}")
    public Result<Void> restoreProject(@PathVariable Long id) {
        int result = projectDao.restore(id);
        if (result > 0) {
            return Result.success();
        }
        return Result.error("恢复项目失败");
    }

    /**
     * 永久删除项目（物理删除）
     */
    @DeleteMapping("/projects/permanent/{id}")
    public Result<Void> permanentDeleteProject(@PathVariable Long id) {
        try {
            int result = projectDao.deleteById(id);
            if (result > 0) {
                return Result.success();
            }
            return Result.error("永久删除项目失败");
        } catch (Exception e) {
            return Result.error("存在关联数据，无法永久删除");
        }
    }

    // ==================== 合同回收站 ====================

    /**
     * 查看已删除的合同列表
     */
    @GetMapping("/contracts/deleted")
    public Result<List<Contract>> getDeletedContracts() {
        List<Contract> deletedContracts = contractDao.selectDeleted();
        return Result.success(deletedContracts);
    }

    /**
     * 恢复已删除的合同
     */
    @PutMapping("/contracts/restore/{id}")
    public Result<Void> restoreContract(@PathVariable Long id) {
        int result = contractDao.restore(id);
        if (result > 0) {
            return Result.success();
        }
        return Result.error("恢复合同失败");
    }

    /**
     * 永久删除合同（物理删除）
     */
    @DeleteMapping("/contracts/permanent/{id}")
    public Result<Void> permanentDeleteContract(@PathVariable Long id) {
        try {
            int result = contractDao.deleteById(id);
            if (result > 0) {
                return Result.success();
            }
            return Result.error("永久删除合同失败");
        } catch (Exception e) {
            return Result.error("存在关联数据，无法永久删除");
        }
    }

    // ==================== 支付回收站 ====================

    /**
     * 查看已删除的支付记录列表
     */
    @GetMapping("/payments/deleted")
    public Result<List<Payment>> getDeletedPayments() {
        List<Payment> deletedPayments = paymentDao.selectDeleted();
        return Result.success(deletedPayments);
    }

    /**
     * 恢复已删除的支付记录
     */
    @PutMapping("/payments/restore/{id}")
    public Result<Void> restorePayment(@PathVariable Long id) {
        int result = paymentDao.restore(id);
        if (result > 0) {
            return Result.success();
        }
        return Result.error("恢复支付记录失败");
    }

    /**
     * 永久删除支付记录（物理删除）
     */
    @DeleteMapping("/payments/permanent/{id}")
    public Result<Void> permanentDeletePayment(@PathVariable Long id) {
        try {
            int result = paymentDao.deleteById(id);
            if (result > 0) {
                return Result.success();
            }
            return Result.error("永久删除支付记录失败");
        } catch (Exception e) {
            return Result.error("存在关联数据，无法永久删除");
        }
    }

    // ==================== 批量操作 ====================

    /**
     * 获取所有模块的已删除数据统计
     */
    @GetMapping("/deleted/stats")
    public Result<DeletedStats> getDeletedStats() {
        DeletedStats stats = new DeletedStats();
        stats.setClientCount(clientDao.selectDeleted().size());
        stats.setProductCount(productDao.selectDeleted().size());
        stats.setProjectCount(projectDao.selectDeleted().size());
        stats.setContractCount(contractDao.selectDeleted().size());
        stats.setPaymentCount(paymentDao.selectDeleted().size());
        return Result.success(stats);
    }

    /**
     * 已删除数据统计内部类
     */
    static class DeletedStats {
        private int clientCount;
        private int productCount;
        private int projectCount;
        private int contractCount;
        private int paymentCount;

        public int getClientCount() {
            return clientCount;
        }

        public void setClientCount(int clientCount) {
            this.clientCount = clientCount;
        }

        public int getProductCount() {
            return productCount;
        }

        public void setProductCount(int productCount) {
            this.productCount = productCount;
        }

        public int getProjectCount() {
            return projectCount;
        }

        public void setProjectCount(int projectCount) {
            this.projectCount = projectCount;
        }

        public int getContractCount() {
            return contractCount;
        }

        public void setContractCount(int contractCount) {
            this.contractCount = contractCount;
        }

        public int getPaymentCount() {
            return paymentCount;
        }

        public void setPaymentCount(int paymentCount) {
            this.paymentCount = paymentCount;
        }
    }
}
