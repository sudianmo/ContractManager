package org.example.contractmanager.controller;

import org.example.contractmanager.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员认证Controller
 */
@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    // 临时管理员账号（实际项目中应该从数据库验证）
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String ADMIN_TOKEN = "admin-secret-token-2026";

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody AdminLoginRequest request) {
        // 验证用户名和密码
        if (!ADMIN_USERNAME.equals(request.getUsername())) {
            return Result.error("用户名不存在");
        }
        
        if (!ADMIN_PASSWORD.equals(request.getPassword())) {
            return Result.error("密码错误");
        }
        
        // 登录成功，返回Token
        Map<String, String> data = new HashMap<>();
        data.put("token", ADMIN_TOKEN);
        data.put("username", ADMIN_USERNAME);
        data.put("role", "admin");
        
        return Result.success(data);
    }

    /**
     * 验证Token是否有效
     */
    @GetMapping("/verify")
    public Result<Map<String, Object>> verify(@RequestHeader("Admin-Token") String token) {
        if (ADMIN_TOKEN.equals(token)) {
            Map<String, Object> data = new HashMap<>();
            data.put("valid", true);
            data.put("username", ADMIN_USERNAME);
            data.put("role", "admin");
            return Result.success(data);
        }
        return Result.error("Token无效");
    }

    /**
     * 登录请求类
     */
    static class AdminLoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
