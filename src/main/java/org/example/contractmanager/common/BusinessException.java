package org.example.contractmanager.common;

/**
 * 自定义业务异常类
 * 用于在业务逻辑中抛出异常
 */
public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
