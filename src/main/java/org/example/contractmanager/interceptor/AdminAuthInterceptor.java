package org.example.contractmanager.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.contractmanager.common.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 管理员权限拦截器
 * 拦截所有 /api/admin/* 请求，验证管理员身份
 */
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    // 临时管理员Token（实际项目中应该从数据库或Redis验证）
    private static final String ADMIN_TOKEN = "admin-secret-token-2026";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 处理OPTIONS预检请求
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        
        // 获取请求头中的Token
        String token = request.getHeader("Admin-Token");
        
        // 验证Token
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未提供管理员凭证\",\"data\":null}");
            return false;
        }
        
        if (!ADMIN_TOKEN.equals(token)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":403,\"message\":\"管理员凭证无效，无权访问\",\"data\":null}");
            return false;
        }
        
        // 验证通过，放行
        return true;
    }
}
