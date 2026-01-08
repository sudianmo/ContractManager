package org.example.contractmanager.service;

import org.example.contractmanager.dto.LoginDTO;
import org.example.contractmanager.entity.User;

/**
 * 用户Service接口
 */
public interface UserService {
    /**
     * 用户登录验证
     */
    User login(LoginDTO loginDTO);
}
