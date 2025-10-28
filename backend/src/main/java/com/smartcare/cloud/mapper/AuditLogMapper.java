package com.smartcare.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartcare.cloud.entity.AuditLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审计日志Mapper接口
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Mapper
public interface AuditLogMapper extends BaseMapper<AuditLog> {
}
