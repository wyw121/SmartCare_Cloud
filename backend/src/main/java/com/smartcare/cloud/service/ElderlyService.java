package com.smartcare.cloud.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smartcare.cloud.dto.ElderlyPageDTO;
import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.vo.HealthStatisticsVO;
import com.smartcare.cloud.vo.ResponseResult;

/**
 * 老人信息服务接口
 *
 * @author SmartCare Team
 * @date 2024-01-01
 */
public interface ElderlyService extends IService<Elderly> {

    /**
     * 分页查询老人信息
     *
     * @param pageDTO 分页查询参数
     * @return 分页结果
     */
    ResponseResult<Page<Elderly>> getElderlyPage(ElderlyPageDTO pageDTO);

    /**
     * 根据ID获取老人信息
     *
     * @param id 老人ID
     * @return 老人信息
     */
    ResponseResult<Elderly> getElderlyById(Long id);

    /**
     * 新增老人信息
     *
     * @param elderly 老人信息
     * @return 操作结果
     */
    ResponseResult<Void> addElderly(Elderly elderly);

    /**
     * 更新老人信息
     *
     * @param elderly 老人信息
     * @return 操作结果
     */
    ResponseResult<Void> updateElderly(Elderly elderly);

    /**
     * 删除老人信息
     *
     * @param id 老人ID
     * @return 操作结果
     */
    ResponseResult<Void> deleteElderly(Long id);

    /**
     * 批量删除老人信息
     *
     * @param ids 老人ID列表
     * @return 操作结果
     */
    ResponseResult<Void> batchDeleteElderly(List<Long> ids);

    /**
     * 获取重点关注老人列表
     *
     * @return 重点关注老人列表
     */
    ResponseResult<List<Elderly>> getKeyElderlyList();

    /**
     * 获取老人健康档案
     *
     * @param id 老人ID
     * @return 健康档案列表
     */
    ResponseResult<Object> getElderlyHealthRecords(Long id);

    /**
     * 添加健康记录
     *
     * @param id 老人ID
     * @param healthRecord 健康记录
     * @return 操作结果
     */
    ResponseResult<Void> addHealthRecord(Long id, Object healthRecord);

    /**
     * 获取老人紧急联系人
     *
     * @param id 老人ID
     * @return 紧急联系人信息
     */
    ResponseResult<Object> getEmergencyContacts(Long id);

    /**
     * 更新紧急联系人
     *
     * @param id 老人ID
     * @param emergencyContacts 紧急联系人信息
     * @return 操作结果
     */
    ResponseResult<Void> updateEmergencyContacts(Long id, Object emergencyContacts);

    /**
     * 获取老人照护计划
     *
     * @param id 老人ID
     * @return 照护计划
     */
    ResponseResult<Object> getCarePlan(Long id);

    /**
     * 更新照护计划
     *
     * @param id 老人ID
     * @param carePlan 照护计划
     * @return 操作结果
     */
    ResponseResult<Void> updateCarePlan(Long id, Object carePlan);

    /**
     * 获取健康预警信息
     *
     * @param id 老人ID
     * @return 预警信息列表
     */
    ResponseResult<Object> getHealthWarnings(Long id);

    /**
     * 生成健康评估报告
     *
     * @param id 老人ID
     * @return 评估报告
     */
    ResponseResult<Object> generateAssessmentReport(Long id);

    /**
     * 批量导出老人档案
     *
     * @param ids 老人ID列表
     * @return 导出数据
     */
    ResponseResult<Object> exportElderlyData(List<Long> ids);

    /**
     * 批量导入老人档案
     *
     * @param importData 导入数据
     * @return 导入结果
     */
    ResponseResult<Object> importElderlyData(Object importData);

    /**
     * 获取照护等级统计
     *
     * @return 统计结果
     */
    ResponseResult<Object> getCareLevelStatistics();

    /**
     * 获取年龄段分布统计
     *
     * @return 统计结果
     */
    ResponseResult<Object> getAgeDistribution();

    /**
     * 搜索老人档案（支持模糊搜索）
     *
     * @param keyword 搜索关键词
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 搜索结果
     */
    ResponseResult<Object> searchElderly(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 获取健康统计数据
     *
     * @return 健康统计数据
     */
    ResponseResult<HealthStatisticsVO> getHealthStatistics();

    /**
     * 获取健康状态分布
     *
     * @return 健康状态分布数据
     */
    ResponseResult<List<HealthStatisticsVO.HealthStatusDistribution>> getHealthStatusDistribution();

    /**
     * 获取年龄段健康分布
     *
     * @return 年龄段健康分布数据
     */
    ResponseResult<List<HealthStatisticsVO.AgeHealthDistribution>> getAgeHealthDistribution();

    /**
     * 获取健康风险评估
     *
     * @return 健康风险评估数据
     */
    ResponseResult<HealthStatisticsVO.HealthRiskAssessment> getHealthRiskAssessment();

    /**
     * 根据老人ID列表批量获取老人信息（家属专用）
     *
     * @param elderlyIds 老人ID列表
     * @return 老人信息列表
     */
    ResponseResult<List<Elderly>> getElderlyByIds(List<Long> elderlyIds);
}
