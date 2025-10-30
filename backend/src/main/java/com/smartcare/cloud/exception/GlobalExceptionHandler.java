package com.smartcare.cloud.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.smartcare.cloud.util.ResponseResult;

/**
 * 全局异常处理器
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseResult<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ResponseResult<Map<String, String>> result = ResponseResult.error("参数验证失败");
        result.setData(errors);
        return ResponseEntity.badRequest().body(result);
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseResult<Map<String, String>>> handleBindException(BindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ResponseResult<Map<String, String>> result = ResponseResult.error("参数绑定失败");
        result.setData(errors);
        return ResponseEntity.badRequest().body(result);
    }

    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseResult<Map<String, String>>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }

        ResponseResult<Map<String, String>> result = ResponseResult.error("约束验证失败");
        result.setData(errors);
        return ResponseEntity.badRequest().body(result);
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseResult<String>> handleBusinessException(BusinessException ex) {
        ResponseResult<String> result = ResponseResult.error(ex.getMessage());
        return ResponseEntity.status(ex.getCode() != null ? ex.getCode() : 500).body(result);
    }

    /**
     * 处理一般异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseResult<String>> handleGeneralException(Exception ex) {
        ResponseResult<String> result = ResponseResult.error("系统错误：" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
