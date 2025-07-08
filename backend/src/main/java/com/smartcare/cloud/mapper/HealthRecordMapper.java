package com.smartcare.cloud.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 根据老人ID查询最新的健康记录
     *
     * @param elderlyId 老人ID
     * @return 最新的健康记录
     */
    @Select("SELECT * FROM health_record "
            + "WHERE elderly_id = #{elderlyId} AND is_deleted = 0 "
            + "ORDER BY record_date DESC LIMIT 1")
    HealthRecord selectLatestByElderlyId(@Param("elderlyId") Long elderlyId);

    /**
     * 根据老人ID和时间范围查询健康记录
     *
     * @param elderlyId 老人ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 健康记录列表
     */
    @Select("SELECT * FROM health_record "
            + "WHERE elderly_id = #{elderlyId} AND is_deleted = 0 "
            + "AND record_date >= #{startTime} AND record_date <= #{endTime} "
            + "ORDER BY record_date DESC")
    List<HealthRecord> selectByElderlyIdAndTimeRange(@Param("elderlyId") Long elderlyId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 根据老人ID查询最近N条健康记录
     *
     * @param elderlyId 老人ID
     * @param limit 限制数量
     * @return 健康记录列表
     */
    @Select("SELECT * FROM health_record "
            + "WHERE elderly_id = #{elderlyId} AND is_deleted = 0 "
            + "ORDER BY record_date DESC LIMIT #{limit}")
    List<HealthRecord> selectRecentByElderlyId(@Param("elderlyId") Long elderlyId,
            @Param("limit") Integer limit);

}
