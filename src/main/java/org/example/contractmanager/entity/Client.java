package org.example.contractmanager.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;

/**
 * 客户实体类
 * 对应数据库中的客户表（Client）
 */
@Data  // Lombok 自动生成 getter/setter/toString/equals/hashCode
@Entity
@Table(name = "Client")
public class Client {
    
    @Id  // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自增
    @Column(name = "id")
    private Long id;
    
    @Column(name = "client_name", nullable = false, unique = true)  // 客户名称（非空、唯一）
    private String clientName;
    
    @Column(name = "contact_person")  // 联系人
    private String contactPerson;
    
    @Column(name = "phone", length = 20)  // 联系电话
    private String phone;
    
    @Column(name = "email", unique = true)  // 邮箱（唯一）
    private String email;
    
    @Column(name = "address")  // 地址
    private String address;
    
    @Column(name = "company_type", length = 50)  // 公司类型（如：国企、民企、外企等）
    private String companyType;
    
    @Column(name = "credit_level", length = 10)  // 信用等级（如：A、B、C等）
    private String creditLevel;
    
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
