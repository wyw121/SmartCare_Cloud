package com.smartcare.cloud.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.smartcare.cloud.annotation.DataPermission;
import com.smartcare.cloud.exception.BusinessException;
import com.smartcare.cloud.service.DoctorElderlyRelationService;
import com.smartcare.cloud.service.FamilyPermissionService;
import com.smartcare.cloud.util.JwtUtil;

/**
 * 数据权限校验切面
 * 
 * 拦截带有@DataPermission注解的方法,根据用户角色自动校验数据访问权限
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Aspect
@Component
public class DataPermissionAspect {

    private static final Logger log = LoggerFactory.getLogger(DataPermissionAspect.class);

    @Autowired
    private DoctorElderlyRelationService doctorElderlyRelationService;

    @Autowired
    private FamilyPermissionService familyPermissionService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 数据权限校验切入点
     * 在方法执行前进行权限校验
     */
    @Before("@annotation(dataPermission)")
    public void checkDataPermission(JoinPoint joinPoint, DataPermission dataPermission) {
        try {
            // 1. 获取当前请求和用户信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                throw new BusinessException("无法获取请求上下文");
            }
            
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader("Authorization");
            
            if (token == null || !token.startsWith("Bearer ")) {
                throw new BusinessException("未授权访问");
            }
            
            token = token.substring(7);
            
            // 2. 解析JWT获取用户信息
            Long userId = jwtUtil.getUserIdFromToken(token);
            String roleCode = jwtUtil.getRoleCodeFromToken(token);
            
            if (userId == null || roleCode == null) {
                throw new BusinessException("无效的访问令牌");
            }
            
            // 3. 管理员跳过权限检查(除非注解明确要求校验)
            if ("system_admin".equals(roleCode) && !dataPermission.skipAdmin()) {
                log.debug("管理员[{}]访问,跳过数据权限校验", userId);
                return;
            }
            
            // 4. 获取老人ID参数
            Long elderlyId = getElderlyIdFromArgs(joinPoint, dataPermission.elderlyIdParam());
            
            if (elderlyId == null) {
                throw new BusinessException("缺少老人ID参数");
            }
            
            // 5. 根据角色进行权限校验
            boolean hasPermission = false;
            
            switch (roleCode) {
                case "doctor":
                    // 医生: 检查是否负责该老人
                    hasPermission = doctorElderlyRelationService.checkDoctorElderlyAccess(userId, elderlyId);
                    if (!hasPermission) {
                        log.warn("权限校验失败: 医生[{}]尝试访问非负责老人[{}]", userId, elderlyId);
                    }
                    break;
                    
                case "family":
                    // 家属: 检查家属关联关系
                    hasPermission = familyPermissionService.checkFamilyElderlyAccess(elderlyId, userId);
                    if (!hasPermission) {
                        log.warn("权限校验失败: 家属[{}]尝试访问非关联老人[{}]", userId, elderlyId);
                    }
                    break;
                    
                case "nurse":
                case "social_worker":
                    // 护工和社工暂时允许访问所有老人(后续可增加数据隔离)
                    hasPermission = true;
                    break;
                    
                default:
                    log.warn("未知角色[{}]尝试访问数据", roleCode);
                    hasPermission = false;
            }
            
            // 6. 权限校验失败抛出异常
            if (!hasPermission) {
                throw new BusinessException("无权访问该老人数据");
            }
            
            log.debug("数据权限校验通过: 用户[{}], 角色[{}], 老人[{}]", userId, roleCode, elderlyId);
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("数据权限校验异常", e);
            throw new BusinessException("权限校验失败: " + e.getMessage());
        }
    }

    /**
     * 从方法参数中提取老人ID
     * 
     * @param joinPoint 切入点
     * @param paramName 参数名
     * @return 老人ID
     */
    private Long getElderlyIdFromArgs(JoinPoint joinPoint, String paramName) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        
        for (int i = 0; i < parameterNames.length; i++) {
            if (paramName.equals(parameterNames[i])) {
                Object value = args[i];
                
                if (value instanceof Long) {
                    return (Long) value;
                } else if (value instanceof Integer) {
                    return ((Integer) value).longValue();
                } else if (value instanceof String) {
                    try {
                        return Long.parseLong((String) value);
                    } catch (NumberFormatException e) {
                        log.warn("无法解析老人ID参数: {}", value);
                    }
                }
            }
        }
        
        return null;
    }
}
