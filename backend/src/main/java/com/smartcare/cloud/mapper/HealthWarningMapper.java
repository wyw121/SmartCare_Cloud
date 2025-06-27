package com.smartcare.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartcare.cloud.entity.HealthWarning;
import org.apache.ibatis.annotations.Mapper;

/**
 * 健康预警数据访问层
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Mapper
public interface HealthWarningMapper extends BaseMapper<HealthWarning> {
    
}
