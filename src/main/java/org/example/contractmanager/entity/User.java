package org.example.contractmanager.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
public class User {
    private Integer userId;
    private String username;
    private String password;
    private LocalDateTime createdAt;
}
