package com.smartcare.cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcare.cloud.entity.AuditLog;
import com.smartcare.cloud.mapper.AuditLogMapper;
import com.smartcare.cloud.service.AuditLogService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 审计日志Service实现
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Slf4j
@Service
public class AuditLogServiceImpl extends ServiceImpl<AuditLogMapper, AuditLog> implements AuditLogService {

    // 专用审计日志Logger
    private static final Logger AUDIT_LOG = LoggerFactory.getLogger("AUDIT_LOG");

    /**
     * 异步保存审计日志
     * 使用@Async注解实现异步保存,避免影响主业务性能
     *
     * @param auditLog 审计日志对象
     */
    @Async
    @Override
    public void saveAuditLogAsync(AuditLog auditLog) {
        try {
            // 保存到数据库
            save(auditLog);
            
            // 同时写入审计日志文件
            AUDIT_LOG.info("操作审计 - 用户: {}, 操作: {}, 模块: {}, 描述: {}, IP: {}, 状态: {}, 耗时: {}ms",
                    auditLog.getUsername(),
                    auditLog.getOperationType(),
                    auditLog.getOperationModule(),
                    auditLog.getOperationDesc(),
                    auditLog.getIpAddress(),
                    auditLog.getOperationStatus() == 1 ? "成功" : "失败",
                    auditLog.getExecutionTime());
        } catch (Exception e) {
            log.error("保存审计日志失败 - 用户: {}, 操作: {}, 错误: {}",
                    auditLog.getUsername(), auditLog.getOperationType(), e.getMessage(), e);
        }
    }
}
