package com.smartcare.cloud.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限校验注解
 * 
 * 用于Controller方法级别,自动校验当前用户对指定老人的访问权限
 * 
 * 使用示例:
 * <pre>
 * {@literal @}DataPermission(elderlyIdParam = "elderlyId")
 * public ResponseResult getElderlyDetail(@PathVariable Long elderlyId) {
 *     // 方法执行前会自动校验权限
 * }
 * </pre>
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {
    
    /**
     * 老人ID参数名称
     * 支持路径参数(@PathVariable)和请求参数(@RequestParam)
     * 
     * @return 参数名
     */
    String elderlyIdParam() default "elderlyId";
    
    /**
     * 是否跳过管理员权限检查
     * true: 管理员也需要数据权限校验
     * false: 管理员跳过权限校验(默认)
     * 
     * @return 是否跳过
     */
    boolean skipAdmin() default false;
}
