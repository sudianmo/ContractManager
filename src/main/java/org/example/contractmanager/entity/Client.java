package org.example.contractmanager.entity;

import lombok.Data;
import java.time.LocalDate;

/**
 * 客户实体类（纯POJO，不使用JPA）
 * 对应数据库Customers表
 */
@Data
public class Client {
    
    private Long id;
    
    private String clientName;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private LocalDate registrationDate;
    private String status;
    

}
