package com.smartcare.cloud.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartcare.cloud.entity.HealthWarning;

/**
 * 健康预警数据访问层
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Mapper
public interface HealthWarningMapper extends BaseMapper<HealthWarning> {

}
