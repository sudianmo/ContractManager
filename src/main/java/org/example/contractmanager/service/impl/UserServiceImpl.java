package org.example.contractmanager.service.impl;

import org.example.contractmanager.common.BusinessException;
import org.example.contractmanager.dao.UserDao;
import org.example.contractmanager.dto.LoginDTO;
import org.example.contractmanager.entity.User;
import org.example.contractmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User login(LoginDTO loginDTO) {
        // 根据用户名查询用户
        User user = userDao.selectByUsername(loginDTO.getUsername());
        
        // 用户不存在
        if (user == null) {
            throw new BusinessException("用户名不存在");
        }
        
        // 密码错误
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new BusinessException("密码错误");
        }
        
        // 登录成功，清空密码字段
        user.setPassword(null);
        return user;
    }
}
