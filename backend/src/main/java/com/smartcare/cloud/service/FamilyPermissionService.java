package com.smartcare.cloud.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.smartcare.cloud.dto.FamilyElderlyAccessDTO;

/**
 * 家属权限服务接口 实现符合医疗信息隐私保护标准的家属权限控制
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
public interface FamilyPermissionService {

    /**
     * 检查家属是否可以访问指定老人信息
     *
     * @param elderlyId 老人ID
     * @param familyId 家属ID
     * @return 是否有访问权限
     */
    boolean checkFamilyElderlyAccess(Long elderlyId, Long familyId);

    /**
     * 获取家属关联的老人列表
     *
     * @param familyId 家属ID
     * @return 老人列表（脱敏后）
     */
    List<FamilyElderlyAccessDTO> getFamilyElderlyList(Long familyId);

    /**
     * 获取家属可查看的老人基本信息
     *
     * @param elderlyId 老人ID
     * @param familyId 家属ID
     * @return 老人基本信息（脱敏后）
     */
    FamilyElderlyAccessDTO getElderlyInfoForFamily(Long elderlyId, Long familyId);

    /**
     * 获取家属可查看的健康概要信息
     *
     * @param elderlyId 老人ID
     * @param familyId 家属ID
     * @return 健康概要信息（脱敏后）
     */
    Map<String, Object> getHealthSummaryForFamily(Long elderlyId, Long familyId);

    /**
     * 获取家属可查看的紧急预警信息
     *
     * @param elderlyId 老人ID
     * @param familyId 家属ID
     * @return 紧急预警信息列表
     */
    List<Map<String, Object>> getEmergencyAlertsForFamily(Long elderlyId, Long familyId);

    /**
     * 处理家属紧急联系
     *
     * @param familyId 家属ID
     * @param elderlyId 老人ID
     * @param message 联系信息
     */
    void handleEmergencyContact(Long familyId, Long elderlyId, String message);

    /**
     * 处理家属探视预约
     *
     * @param familyId 家属ID
     * @param elderlyId 老人ID
     * @param visitDate 探视日期
     * @param visitTime 探视时间
     * @return 预约结果
     */
    Map<String, Object> scheduleVisit(Long familyId, Long elderlyId, String visitDate, String visitTime);

    /**
     * 记录家属操作日志
     *
     * @param familyId 家属ID
     * @param action 操作类型
     * @param details 操作详情
     * @param request HTTP请求对象
     */
    void logFamilyAction(Long familyId, String action, Map<String, Object> details, HttpServletRequest request);

    /**
     * 对敏感信息进行脱敏处理
     *
     * @param data 原始数据
     * @param dataType 数据类型
     * @return 脱敏后的数据
     */
    Map<String, Object> maskSensitiveData(Map<String, Object> data, String dataType);

    /**
     * 检查家属是否有指定时间范围的数据访问权限
     *
     * @param familyId 家属ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 是否有权限
     */
    boolean checkTimeRangeAccess(Long familyId, String startTime, String endTime);
}
