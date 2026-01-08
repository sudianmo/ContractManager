package org.example.contractmanager.controller;

import org.example.contractmanager.common.Result;
import org.example.contractmanager.dto.LoginDTO;
import org.example.contractmanager.entity.User;
import org.example.contractmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户Controller
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(originPatterns = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody LoginDTO loginDTO) {
        User user = userService.login(loginDTO);
        return Result.success(user);
    }
}
