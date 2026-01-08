package org.example.contractmanager.dao;

import org.example.contractmanager.entity.User;

/**
 * 用户DAO接口
 */
public interface UserDao {
    /**
     * 根据用户名查询用户
     */
    User selectByUsername(String username);
}
