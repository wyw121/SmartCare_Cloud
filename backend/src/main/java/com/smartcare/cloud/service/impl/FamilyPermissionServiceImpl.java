package com.smartcare.cloud.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcare.cloud.dto.FamilyElderlyAccessDTO;
import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.entity.FamilyElderlyRelation;
import com.smartcare.cloud.entity.HealthRecord;
import com.smartcare.cloud.entity.HealthWarning;
import com.smartcare.cloud.mapper.ElderlyMapper;
import com.smartcare.cloud.mapper.FamilyElderlyRelationMapper;
import com.smartcare.cloud.mapper.HealthRecordMapper;
import com.smartcare.cloud.mapper.HealthWarningMapper;
import com.smartcare.cloud.service.FamilyPermissionService;

/**
 * 家属权限服务实现类 实现符合医疗信息隐私保护标准的家属权限控制
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Service
public class FamilyPermissionServiceImpl implements FamilyPermissionService {

    @Autowired
    private ElderlyMapper elderlyMapper;

    @Autowired
    private FamilyElderlyRelationMapper familyElderlyRelationMapper;

    @Autowired
    private HealthRecordMapper healthRecordMapper;

    @Autowired
    private HealthWarningMapper healthWarningMapper;

    @Override
    public boolean checkFamilyElderlyAccess(Long elderlyId, Long familyId) {
        try {
            // 查询家属与老人的关联关系
            FamilyElderlyRelation relation = familyElderlyRelationMapper.selectByFamilyIdAndElderlyId(familyId, elderlyId);

            // 检查关联关系是否存在且有效
            if (relation == null || relation.getStatus() == null || relation.getStatus() != 1) {
                return false;
            }

            // 检查关联关系是否在有效期内
            LocalDateTime now = LocalDateTime.now();
            if (relation.getStartDate() != null && now.isBefore(relation.getStartDate())) {
                return false;
            }
            if (relation.getEndDate() != null && now.isAfter(relation.getEndDate())) {
                return false;
            }

            return true;
        } catch (Exception e) {
            // 出现异常时默认拒绝访问
            return false;
        }
    }

    @Override
    public List<FamilyElderlyAccessDTO> getFamilyElderlyList(Long familyId) {
        try {
            // 获取家属关联的老人ID列表
            List<Long> elderlyIds = familyElderlyRelationMapper.selectElderlyIdsByFamilyId(familyId);

            if (elderlyIds.isEmpty()) {
                return new ArrayList<>();
            }

            // 获取老人基本信息
            List<Elderly> elderlyList = elderlyMapper.selectBatchIds(elderlyIds);

            // 转换为家属可查看的DTO
            return elderlyList.stream()
                    .map(elderly -> convertToFamilyDTO(elderly, familyId))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public FamilyElderlyAccessDTO getElderlyInfoForFamily(Long elderlyId, Long familyId) {
        try {
            // 获取老人基本信息
            Elderly elderly = elderlyMapper.selectById(elderlyId);
            if (elderly == null) {
                return null;
            }

            // 转换为家属可查看的DTO
            return convertToFamilyDTO(elderly, familyId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Map<String, Object> getHealthSummaryForFamily(Long elderlyId, Long familyId) {
        try {
            // 获取最近的健康记录
            HealthRecord latestRecord = healthRecordMapper.selectLatestByElderlyId(elderlyId);

            Map<String, Object> summary = new HashMap<>();

            if (latestRecord != null) {
                // 生成健康概要信息（脱敏）
                summary.put("generalStatus", getHealthStatusText(latestRecord.getDiagnosis()));
                summary.put("lastCheckupDate", latestRecord.getRecordDate());
                summary.put("healthTrend", getHealthTrendText(latestRecord.getIsAbnormal()));
                summary.put("alertLevel", getAlertLevelText(latestRecord.getUrgencyLevel()));
                summary.put("summary", generateHealthSummaryText(latestRecord));
            } else {
                summary.put("generalStatus", "暂无健康记录");
                summary.put("lastCheckupDate", null);
                summary.put("healthTrend", "无");
                summary.put("alertLevel", "无");
                summary.put("summary", "暂无健康记录，建议进行健康检查");
            }

            return summary;
        } catch (Exception e) {
            Map<String, Object> errorSummary = new HashMap<>();
            errorSummary.put("generalStatus", "获取健康信息失败");
            errorSummary.put("summary", "系统异常，请稍后再试");
            return errorSummary;
        }
    }

    @Override
    public List<Map<String, Object>> getEmergencyAlertsForFamily(Long elderlyId, Long familyId) {
        try {
            // 获取最近30天的紧急预警信息
            LocalDateTime startTime = LocalDateTime.now().minusDays(30);
            List<HealthWarning> warnings = healthWarningMapper.selectByElderlyIdAndTimeRange(elderlyId, startTime, LocalDateTime.now());

            // 只返回紧急级别的预警
            return warnings.stream()
                    .filter(warning -> "紧急".equals(warning.getWarningLevel()) || "高".equals(warning.getWarningLevel()))
                    .map(this::convertWarningToFamilyView)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void handleEmergencyContact(Long familyId, Long elderlyId, String message) {
        try {
            // 这里应该实现紧急联系逻辑
            // 1. 通知相关医护人员
            // 2. 记录紧急联系日志
            // 3. 可能的话发送短信/邮件通知

            // 暂时只记录日志
            Map<String, Object> contactDetails = new HashMap<>();
            contactDetails.put("elderlyId", elderlyId);
            contactDetails.put("message", message);
            contactDetails.put("contactTime", LocalDateTime.now());

            logFamilyAction(familyId, "emergency_contact", contactDetails, null);
        } catch (Exception e) {
            // 记录错误日志
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("elderlyId", elderlyId);
            errorDetails.put("error", e.getMessage());
            logFamilyAction(familyId, "emergency_contact_failed", errorDetails, null);
        }
    }

    @Override
    public Map<String, Object> scheduleVisit(Long familyId, Long elderlyId, String visitDate, String visitTime) {
        try {
            Map<String, Object> result = new HashMap<>();

            // 这里应该实现探视预约逻辑
            // 1. 检查探视时间是否可用
            // 2. 创建探视预约记录
            // 3. 通知相关工作人员
            // 暂时模拟成功预约
            result.put("success", true);
            result.put("message", "探视预约成功");
            result.put("visitDate", visitDate);
            result.put("visitTime", visitTime);
            result.put("confirmationNumber", "V" + System.currentTimeMillis());

            // 记录预约日志
            Map<String, Object> visitDetails = new HashMap<>();
            visitDetails.put("elderlyId", elderlyId);
            visitDetails.put("visitDate", visitDate);
            visitDetails.put("visitTime", visitTime);
            logFamilyAction(familyId, "schedule_visit", visitDetails, null);

            return result;
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "探视预约失败：" + e.getMessage());
            return errorResult;
        }
    }

    @Override
    public void logFamilyAction(Long familyId, String action, Map<String, Object> details, HttpServletRequest request) {
        try {
            // 这里应该实现日志记录逻辑
            // 1. 记录到数据库
            // 2. 记录到日志文件
            // 3. 可能的话发送到日志服务

            // 暂时输出到控制台
            System.out.println("家属操作日志: "
                    + "familyId=" + familyId
                    + ", action=" + action
                    + ", details=" + details
                    + ", time=" + LocalDateTime.now());
        } catch (Exception e) {
            // 记录日志失败，输出错误信息
            System.err.println("记录家属操作日志失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> maskSensitiveData(Map<String, Object> data, String dataType) {
        if (data == null) {
            return new HashMap<>();
        }

        Map<String, Object> maskedData = new HashMap<>(data);

        switch (dataType) {
            case "personal":
                // 个人信息脱敏
                if (maskedData.containsKey("idCard")) {
                    maskedData.put("idCard", maskIdCard((String) maskedData.get("idCard")));
                }
                if (maskedData.containsKey("phone")) {
                    maskedData.put("phone", maskPhone((String) maskedData.get("phone")));
                }
                break;
            case "medical":
                // 医疗信息脱敏
                if (maskedData.containsKey("diagnosis")) {
                    maskedData.put("diagnosis", "详细诊断信息需联系医生查看");
                }
                if (maskedData.containsKey("medication")) {
                    maskedData.put("medication", "用药信息需联系医生查看");
                }
                break;
            case "financial":
                // 财务信息完全隐藏
                maskedData.remove("cost");
                maskedData.remove("payment");
                maskedData.remove("insurance");
                break;
        }

        return maskedData;
    }

    @Override
    public boolean checkTimeRangeAccess(Long familyId, String startTime, String endTime) {
        try {
            LocalDateTime start = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime end = LocalDateTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime now = LocalDateTime.now();

            // 家属只能查看最近30天的数据
            LocalDateTime earliestAllowed = now.minusDays(30);
            if (start.isBefore(earliestAllowed)) {
                return false;
            }

            // 家属只能查看未来7天的数据
            LocalDateTime latestAllowed = now.plusDays(7);
            if (end.isAfter(latestAllowed)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将老人信息转换为家属可查看的DTO
     */
    private FamilyElderlyAccessDTO convertToFamilyDTO(Elderly elderly, Long familyId) {
        FamilyElderlyAccessDTO dto = new FamilyElderlyAccessDTO();
        dto.setElderlyId(elderly.getId());
        dto.setName(elderly.getName());
        dto.setAge(elderly.getAge());
        dto.setGender(getGenderText(elderly.getGender()));
        dto.setRoom(elderly.getRoomNumber());
        dto.setStatus(getHealthStatusText(elderly.getHealthStatus()));
        dto.setPhone(maskPhone(elderly.getPhone()));
        dto.setAdmissionTime(elderly.getCreateTime());

        // 获取关联关系信息
        FamilyElderlyRelation relation = familyElderlyRelationMapper.selectByFamilyIdAndElderlyId(familyId, elderly.getId());
        if (relation != null) {
            dto.setRelationship(relation.getRelationshipName());
            dto.setAccessLevel(relation.getAccessLevel());
        }

        // 获取健康概要
        HealthRecord latestRecord = healthRecordMapper.selectLatestByElderlyId(elderly.getId());
        if (latestRecord != null) {
            dto.setHealthSummary(generateHealthSummaryText(latestRecord));
            dto.setLastCheckupTime(latestRecord.getRecordDate());
            dto.setAlertLevel(getAlertLevelText(latestRecord.getUrgencyLevel()));
        }

        return dto;
    }

    /**
     * 将预警信息转换为家属可查看的格式
     */
    private Map<String, Object> convertWarningToFamilyView(HealthWarning warning) {
        Map<String, Object> familyView = new HashMap<>();
        familyView.put("warningId", warning.getId());
        familyView.put("warningLevel", warning.getWarningLevel());
        familyView.put("warningType", warning.getWarningType());
        familyView.put("warningMessage", warning.getContent());
        familyView.put("warningTime", warning.getCreateTime());
        familyView.put("status", warning.getStatus());
        // 不包含详细的医疗信息
        return familyView;
    }

    /**
     * 生成健康概要文本
     */
    private String generateHealthSummaryText(HealthRecord record) {
        if (record == null) {
            return "暂无健康记录";
        }

        StringBuilder summary = new StringBuilder();

        String status = getHealthStatusText(record.getDiagnosis());
        summary.append("健康状况：").append(status);

        if (record.getIsAbnormal() != null) {
            summary.append("，趋势：").append(getHealthTrendText(record.getIsAbnormal()));
        }

        if (record.getUrgencyLevel() != null && record.getUrgencyLevel() > 0) {
            summary.append("，预警级别：").append(getAlertLevelText(record.getUrgencyLevel()));
        }

        return summary.toString();
    }

    /**
     * 获取健康状态文本
     */
    private String getHealthStatusText(String status) {
        if (status == null) {
            return "未知";
        }
        switch (status.toLowerCase()) {
            case "good":
                return "良好";
            case "fair":
                return "一般";
            case "poor":
                return "较差";
            case "critical":
                return "危急";
            default:
                return "未知";
        }
    }

    /**
     * 获取健康趋势文本
     */
    private String getHealthTrendText(Integer isAbnormal) {
        if (isAbnormal == null) {
            return "稳定";
        }
        switch (isAbnormal) {
            case 0:
                return "稳定";
            case 1:
                return "异常";
            default:
                return "稳定";
        }
    }

    /**
     * 获取预警级别文本
     */
    private String getAlertLevelText(Integer level) {
        if (level == null) {
            return "无";
        }
        switch (level) {
            case 1:
                return "低";
            case 2:
                return "中";
            case 3:
                return "高";
            case 4:
                return "紧急";
            default:
                return "无";
        }
    }

    /**
     * 手机号脱敏
     */
    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 身份证号脱敏
     */
    private String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 18) {
            return idCard;
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(14);
    }

    /**
     * 获取性别文本
     */
    private String getGenderText(Integer gender) {
        if (gender == null) {
            return "未知";
        }
        switch (gender) {
            case 1:
                return "男";
            case 0:
                return "女";
            default:
                return "未知";
        }
    }
}
