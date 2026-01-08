package org.example.contractmanager.entity;

import lombok.Data;
import java.time.LocalDate;

/**
 * 客户实体类（纯POJO，使用JDBC）
 * 对应数据库Customers表
 */
@Data
public class Customer {
    
    private Long customerId;
    private String customerName;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private LocalDate registrationDate;
    private String status;
    

}
