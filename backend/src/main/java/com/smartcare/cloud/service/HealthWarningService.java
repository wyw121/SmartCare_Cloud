package com.smartcare.cloud.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.smartcare.cloud.dto.HealthWarningPageDTO;
import com.smartcare.cloud.entity.HealthWarning;

/**
 * 健康预警服务接口
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
public interface HealthWarningService extends IService<HealthWarning> {

    /**
     * 分页查询健康预警列表
     *
     * @param dto 查询条件
     * @return 分页结果
     */
    PageInfo<HealthWarning> getPageList(HealthWarningPageDTO dto);

    /**
     * 根据老人ID查询预警列表
     *
     * @param elderlyId 老人ID
     * @return 预警列表
     */
    List<HealthWarning> getByElderlyId(Long elderlyId);

    /**
     * 根据状态查询预警列表
     *
     * @param status 状态
     * @return 预警列表
     */
    List<HealthWarning> getByStatus(Integer status);

    /**
     * 根据预警级别查询预警列表
     *
     * @param warningLevel 预警级别
     * @return 预警列表
     */
    List<HealthWarning> getByWarningLevel(Integer warningLevel);

    /**
     * 处理预警
     *
     * @param id 预警ID
     * @param handlerId 处理人ID
     * @param handlerName 处理人姓名
     * @param handleRemark 处理备注
     * @return 处理结果
     */
    boolean handleWarning(Long id, Long handlerId, String handlerName, String handleRemark);

    /**
     * 更新预警状态
     *
     * @param id 预警ID
     * @param status 状态
     * @return 更新结果
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 批量删除预警
     *
     * @param ids 预警ID列表
     * @return 删除结果
     */
    boolean deleteBatch(List<Long> ids);

    /**
     * 获取未处理预警数量
     *
     * @return 未处理预警数量
     */
    long getUnhandledCount();

    /**
     * 统计各级别预警数量
     *
     * @return 统计结果
     */
    List<Object> getWarningLevelStatistics();

    /**
     * 统计预警类型分布
     *
     * @return 统计结果
     */
    List<Object> getWarningTypeStatistics();

    /**
     * 根据预警类型查询预警列表
     *
     * @param warningType 预警类型
     * @return 预警列表
     */
    List<HealthWarning> getByWarningType(String warningType);

    /**
     * 创建预警
     *
     * @param elderlyId 老人ID
     * @param elderlyName 老人姓名
     * @param warningType 预警类型
     * @param warningLevel 预警级别
     * @param title 预警标题
     * @param content 预警内容
     * @param triggerData 触发数据
     * @param dataSource 数据来源
     * @return 创建结果
     */
    boolean createWarning(Long elderlyId, String elderlyName, String warningType,
            Integer warningLevel, String title, String content,
            String triggerData, String dataSource);

    /**
     * 根据老人ID列表分页查询健康预警列表（家属专用）
     *
     * @param dto 查询条件
     * @param elderlyIds 允许查询的老人ID列表
     * @return 分页结果
     */
    PageInfo<HealthWarning> getPageListByElderlyIds(HealthWarningPageDTO dto, List<Long> elderlyIds);

    /**
     * 根据老人ID列表获取健康预警统计数据（家属专用）
     *
     * @param elderlyIds 老人ID列表
     * @return 统计结果
     */
    Map<String, Object> getWarningStatisticsByElderlyIds(List<Long> elderlyIds);
}
