package org.example.contractmanager.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 合同实体类
 * 对应数据库中的合同表（Contract）
 */
@Data  // Lombok 自动生成 getter/setter/toString/equals/hashCode
@Entity
@Table(name = "Contract")
public class Contract {
    
    @Id  // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自增
    @Column(name = "id")
    private Long id;
    
    @Column(name = "contract_no", unique = true)  // 合同编号（唯一）
    private String contractNo;
    
    @Column(name = "contract_name", nullable = false)  // 合同名称（非空）
    private String contractName;
    
    @Column(name = "client_id")  // 客户ID（外键）
    private Long clientId;
    
    @Column(name = "amount", precision = 18, scale = 2)  // 合同金额
    private BigDecimal amount;
    
    @Column(name = "sign_date")  // 签订日期
    private LocalDate signDate;
    
    @Column(name = "start_date")  // 开始日期
    private LocalDate startDate;
    
    @Column(name = "end_date")  // 结束日期
    private LocalDate endDate;
    
    @Column(name = "status", length = 20, columnDefinition = "VARCHAR(20) DEFAULT '草稿'")  // 合同状态
    private String status;
    
    @Column(name = "description", columnDefinition = "TEXT")  // 合同描述
    private String description;
    
    @Column(name = "create_time", updatable = false)  // 创建时间（不可更新）
    private LocalDate createTime;
    
    @Column(name = "update_time")  // 更新时间
    private LocalDate updateTime;
    
    // JPA 会自动在保存前设置创建时间
    @PrePersist
    protected void onCreate() {
        createTime = LocalDate.now();
        updateTime = LocalDate.now();
    }
    
    // JPA 会自动在更新前设置更新时间
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDate.now();
    }
}
