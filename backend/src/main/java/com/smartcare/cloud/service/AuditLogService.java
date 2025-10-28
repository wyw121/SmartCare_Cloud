package com.smartcare.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartcare.cloud.entity.AuditLog;

/**
 * 审计日志Service接口
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
public interface AuditLogService extends IService<AuditLog> {

    /**
     * 记录审计日志(异步)
     *
     * @param auditLog 审计日志对象
     */
    void saveAuditLogAsync(AuditLog auditLog);
}
