package com.smartcare.cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smartcare.cloud.entity.HealthRecord;
import com.smartcare.cloud.vo.ResponseResult;

/**
 * 健康记录服务接口
 *
 * @author SmartCare Cloud Team
 * @since 2024-01-01
 */
public interface HealthRecordService extends IService<HealthRecord> {

    /**
     * 分页查询老人健康记录
     *
     * @param elderlyId 老人ID
     * @param current 当前页
     * @param size 页大小
     * @param recordType 记录类型
     * @return 分页结果
     */
    ResponseResult<IPage<HealthRecord>> getHealthRecordsByElderlyId(Long elderlyId, Long current, Long size, Integer recordType);

    /**
     * 添加健康记录
     *
     * @param healthRecord 健康记录
     * @return 操作结果
     */
    ResponseResult<Void> addHealthRecord(HealthRecord healthRecord);

    /**
     * 更新健康记录
     *
     * @param healthRecord 健康记录
     * @return 操作结果
     */
    ResponseResult<Void> updateHealthRecord(HealthRecord healthRecord);

    /**
     * 删除健康记录
     *
     * @param id 记录ID
     * @return 操作结果
     */
    ResponseResult<Void> deleteHealthRecord(Long id);

    /**
     * 获取健康记录详情
     *
     * @param id 记录ID
     * @return 健康记录
     */
    ResponseResult<HealthRecord> getHealthRecordById(Long id);

    /**
     * 获取老人最新生命体征
     *
     * @param elderlyId 老人ID
     * @return 最新生命体征记录
     */
    ResponseResult<HealthRecord> getLatestVitalSigns(Long elderlyId);

    /**
     * 获取老人健康记录统计
     *
     * @param elderlyId 老人ID
     * @return 统计数据
     */
    ResponseResult<Object> getHealthRecordStatistics(Long elderlyId);
}
