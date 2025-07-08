package com.smartcare.cloud.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 根据老人ID和时间范围查询健康预警
     *
     * @param elderlyId 老人ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 健康预警列表
     */
    @Select("SELECT * FROM health_warning "
            + "WHERE elderly_id = #{elderlyId} AND is_deleted = 0 "
            + "AND created_time >= #{startTime} AND created_time <= #{endTime} "
            + "ORDER BY created_time DESC")
    List<HealthWarning> selectByElderlyIdAndTimeRange(@Param("elderlyId") Long elderlyId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 根据老人ID查询最近N条健康预警
     *
     * @param elderlyId 老人ID
     * @param limit 限制数量
     * @return 健康预警列表
     */
    @Select("SELECT * FROM health_warning "
            + "WHERE elderly_id = #{elderlyId} AND is_deleted = 0 "
            + "ORDER BY created_time DESC LIMIT #{limit}")
    List<HealthWarning> selectRecentByElderlyId(@Param("elderlyId") Long elderlyId,
            @Param("limit") Integer limit);

    /**
     * 根据老人ID查询活跃的健康预警
     *
     * @param elderlyId 老人ID
     * @return 活跃的健康预警列表
     */
    @Select("SELECT * FROM health_warning "
            + "WHERE elderly_id = #{elderlyId} AND is_deleted = 0 "
            + "AND status = 1 "
            + "ORDER BY created_time DESC")
    List<HealthWarning> selectActiveByElderlyId(@Param("elderlyId") Long elderlyId);

}
