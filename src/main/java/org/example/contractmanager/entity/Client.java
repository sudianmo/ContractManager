package org.example.contractmanager.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "Client")
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "client_name", nullable = false, unique = true)
    private String clientName;
    
    @Column(name = "contact_person")
    private String contactPerson;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "email", unique = true)
    private String email;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "company_type", length = 50)
    private String companyType;
    
    @Column(name = "credit_level", length = 10)
    private String creditLevel;
    
    @Column(name = "create_time", updatable = false)
    private LocalDate createTime;
    
    @Column(name = "update_time")
    private LocalDate updateTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDate.now();
        updateTime = LocalDate.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getCreditLevel() {
        return creditLevel;
    }

    public void setCreditLevel(String creditLevel) {
        this.creditLevel = creditLevel;
    }

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public LocalDate getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDate updateTime) {
        this.updateTime = updateTime;
    }
}
