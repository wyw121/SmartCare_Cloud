package com.smartcare.cloud.annotation;

import java.lang.annotation.*;

/**
 * 审计日志注解
 * 用于标记需要记录审计日志的方法
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {

    /**
     * 操作模块
     * 例如: ELDERLY, DOCTOR, HEALTH_WARNING
     */
    String module();

    /**
     * 操作类型
     * 例如: CREATE, UPDATE, DELETE, EXPORT
     */
    String type();

    /**
     * 操作描述
     * 支持SpEL表达式, 例如: "创建老人档案：#elderly.name"
     */
    String description() default "";
}
