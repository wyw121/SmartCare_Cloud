package com.smartcare.cloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 操作审计日志实体
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("audit_log")
public class AuditLog {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 操作类型
     * LOGIN-登录, LOGOUT-登出, CREATE-新增, UPDATE-修改, DELETE-删除, EXPORT-导出, IMPORT-导入
     */
    private String operationType;

    /**
     * 操作模块
     * ELDERLY-老人管理, DOCTOR-医生管理, HEALTH_WARNING-健康预警等
     */
    private String operationModule;

    /**
     * 操作描述
     */
    private String operationDesc;

    /**
     * 请求方法: GET, POST, PUT, DELETE
     */
    private String requestMethod;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 请求参数(JSON格式)
     */
    private String requestParams;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 用户代理(浏览器信息)
     */
    private String userAgent;

    /**
     * 操作状态: 1-成功, 0-失败
     */
    private Integer operationStatus;

    /**
     * 错误信息(失败时记录)
     */
    private String errorMessage;

    /**
     * 执行时间(毫秒)
     */
    private Integer executionTime;

    /**
     * 操作时间
     */
    private LocalDateTime createdTime;
}
