package com.smartcare.cloud.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartcare.cloud.entity.HealthRecord;

/**
 * 健康记录 Mapper 接口
 *
 * @author SmartCare Cloud Team
 * @since 2024-01-01
 */
@Mapper
public interface HealthRecordMapper extends BaseMapper<HealthRecord> {

}
