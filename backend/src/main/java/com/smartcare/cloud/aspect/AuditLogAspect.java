package com.smartcare.cloud.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcare.cloud.annotation.AuditLog;
import com.smartcare.cloud.service.AuditLogService;
import com.smartcare.cloud.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 审计日志切面
 * 拦截标记@AuditLog注解的方法,记录操作日志
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {

    private static final String UNKNOWN_IP = "unknown";
    
    private final AuditLogService auditLogService;
    private final ObjectMapper objectMapper;
    
    // SpEL表达式解析器
    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 环绕通知 - 记录审计日志
     */
    @Around("@annotation(com.smartcare.cloud.annotation.AuditLog)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        
        // 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AuditLog auditLogAnnotation = method.getAnnotation(AuditLog.class);
        
        // 构建审计日志对象
        com.smartcare.cloud.entity.AuditLog auditLog = new com.smartcare.cloud.entity.AuditLog();
        auditLog.setOperationType(auditLogAnnotation.type());
        auditLog.setOperationModule(auditLogAnnotation.module());
        auditLog.setCreatedTime(LocalDateTime.now());
        
        // 获取用户信息
        if (request != null) {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                try {
                    String username = JwtUtil.getUsernameFromToken(token);
                    auditLog.setUsername(username);
                    // TODO: 从token中获取userId
                } catch (Exception e) {
                    log.warn("解析token失败: {}", e.getMessage());
                }
            }
            
            // 请求信息
            auditLog.setRequestMethod(request.getMethod());
            auditLog.setRequestUrl(request.getRequestURI());
            auditLog.setIpAddress(getIpAddress(request));
            auditLog.setUserAgent(request.getHeader("User-Agent"));
        }
        
        // 解析操作描述(支持SpEL表达式)
        String description = parseDescription(auditLogAnnotation.description(), method, joinPoint.getArgs());
        auditLog.setOperationDesc(description);
        
        // 请求参数(JSON格式)
        try {
            String params = objectMapper.writeValueAsString(joinPoint.getArgs());
            // 限制参数长度,避免过长
            if (params.length() > 2000) {
                params = params.substring(0, 2000) + "...";
            }
            auditLog.setRequestParams(params);
        } catch (Exception e) {
            log.warn("序列化请求参数失败: {}", e.getMessage());
        }
        
        // 执行目标方法
        Object result;
        try {
            result = joinPoint.proceed();
            auditLog.setOperationStatus(1); // 成功
        } catch (Exception e) {
            auditLog.setOperationStatus(0); // 失败
            auditLog.setErrorMessage(e.getMessage());
            throw e;
        } finally {
            // 计算执行时间
            long executionTime = System.currentTimeMillis() - startTime;
            auditLog.setExecutionTime((int) executionTime);
            
            // 异步保存审计日志
            auditLogService.saveAuditLogAsync(auditLog);
        }
        
        return result;
    }

    /**
     * 解析操作描述(支持SpEL表达式)
     */
    private String parseDescription(String descriptionTemplate, Method method, Object[] args) {
        if (descriptionTemplate == null || descriptionTemplate.isEmpty()) {
            return "";
        }
        
        try {
            // 创建SpEL上下文
            EvaluationContext context = new StandardEvaluationContext();
            
            // 获取方法参数名
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            if (parameterNames != null) {
                for (int i = 0; i < parameterNames.length; i++) {
                    context.setVariable(parameterNames[i], args[i]);
                }
            }
            
            // 解析表达式
            Expression expression = parser.parseExpression(descriptionTemplate);
            Object value = expression.getValue(context);
            return value != null ? value.toString() : descriptionTemplate;
        } catch (Exception e) {
            log.warn("解析操作描述失败: {}", e.getMessage());
            return descriptionTemplate;
        }
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 对于多个代理的情况,第一个IP为客户端真实IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }
}
