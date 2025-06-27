package com.smartcare.cloud.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.entity.HealthWarning;
import com.smartcare.cloud.service.DoctorService;
import com.smartcare.cloud.service.ElderlyService;
import com.smartcare.cloud.service.HealthWarningService;
import com.smartcare.cloud.service.ReportStatisticsService;

/**
 * 统计报表服务实现类
 *
 * @author SmartCare Team
 * @since 2025-06-27
 */
@Service
public class ReportStatisticsServiceImpl implements ReportStatisticsService {

    private static final Logger log = LoggerFactory.getLogger(ReportStatisticsServiceImpl.class);

    @Autowired
    private ElderlyService elderlyService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private HealthWarningService healthWarningService;

    @Override
    public Map<String, Object> getOverviewStatistics() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取老人总数
            long elderlyTotal = elderlyService.count();
            result.put("elderlyTotal", elderlyTotal);

            // 获取医生总数
            long doctorTotal = doctorService.count();
            result.put("doctorTotal", doctorTotal);

            // 获取今日新增老人数
            QueryWrapper<Elderly> todayElderlyQuery = new QueryWrapper<>();
            todayElderlyQuery.ge("create_time", LocalDateTime.now().toLocalDate());
            long todayElderlyCount = elderlyService.count(todayElderlyQuery);
            result.put("todayElderlyCount", todayElderlyCount);

            // 获取健康预警总数
            long warningTotal = healthWarningService.count();
            result.put("warningTotal", warningTotal);

            // 获取未处理预警数
            QueryWrapper<HealthWarning> unhandledQuery = new QueryWrapper<>();
            unhandledQuery.eq("status", 0);
            long unhandledWarnings = healthWarningService.count(unhandledQuery);
            result.put("unhandledWarnings", unhandledWarnings);

            // 获取今日新增预警数
            QueryWrapper<HealthWarning> todayWarningQuery = new QueryWrapper<>();
            todayWarningQuery.ge("create_time", LocalDateTime.now().toLocalDate());
            long todayWarnings = healthWarningService.count(todayWarningQuery);
            result.put("todayWarnings", todayWarnings);

            // 计算健康管理覆盖率
            double coverageRate = elderlyTotal > 0 ? (double) elderlyTotal / (elderlyTotal + 100) * 100 : 0;
            result.put("healthCoverageRate", Math.round(coverageRate * 100.0) / 100.0);

            // 计算预警处理率
            double handlingRate = warningTotal > 0 ? (double) (warningTotal - unhandledWarnings) / warningTotal * 100 : 0;
            result.put("warningHandlingRate", Math.round(handlingRate * 100.0) / 100.0);

            log.info("数据概览统计获取成功");

        } catch (Exception e) {
            log.error("获取数据概览统计失败", e);
            throw new RuntimeException("获取数据概览统计失败", e);
        }

        return result;
    }

    @Override
    public Map<String, Object> getHealthStatusStatistics() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取健康状况分布
            List<Map<String, Object>> healthStatusList = new ArrayList<>();

            // 模拟健康状况统计数据（实际项目中应从数据库查询）
            Map<String, Object> excellent = new HashMap<>();
            excellent.put("status", "优良");
            excellent.put("count", 45);
            excellent.put("percentage", 45.0);
            healthStatusList.add(excellent);

            Map<String, Object> good = new HashMap<>();
            good.put("status", "良好");
            good.put("count", 30);
            good.put("percentage", 30.0);
            healthStatusList.add(good);

            Map<String, Object> fair = new HashMap<>();
            fair.put("status", "一般");
            fair.put("count", 20);
            fair.put("percentage", 20.0);
            healthStatusList.add(fair);

            Map<String, Object> poor = new HashMap<>();
            poor.put("status", "较差");
            poor.put("count", 5);
            poor.put("percentage", 5.0);
            healthStatusList.add(poor);

            result.put("healthStatusDistribution", healthStatusList);

            // 获取常见疾病统计
            List<Map<String, Object>> diseaseList = new ArrayList<>();

            String[] diseases = {"高血压", "糖尿病", "心脏病", "关节炎", "认知障碍"};
            int[] diseaseCounts = {35, 28, 15, 12, 8};

            for (int i = 0; i < diseases.length; i++) {
                Map<String, Object> disease = new HashMap<>();
                disease.put("name", diseases[i]);
                disease.put("count", diseaseCounts[i]);
                disease.put("percentage", Math.round((double) diseaseCounts[i] / 100 * 100 * 100.0) / 100.0);
                diseaseList.add(disease);
            }

            result.put("commonDiseases", diseaseList);

            // 获取年龄段分布
            List<Map<String, Object>> ageDistribution = new ArrayList<>();

            String[] ageGroups = {"60-65岁", "66-70岁", "71-75岁", "76-80岁", "80岁以上"};
            int[] ageCounts = {20, 25, 22, 18, 15};

            for (int i = 0; i < ageGroups.length; i++) {
                Map<String, Object> ageGroup = new HashMap<>();
                ageGroup.put("ageGroup", ageGroups[i]);
                ageGroup.put("count", ageCounts[i]);
                ageGroup.put("percentage", Math.round((double) ageCounts[i] / 100 * 100 * 100.0) / 100.0);
                ageDistribution.add(ageGroup);
            }

            result.put("ageDistribution", ageDistribution);

            log.info("健康状况统计获取成功");

        } catch (Exception e) {
            log.error("获取健康状况统计失败", e);
            throw new RuntimeException("获取健康状况统计失败", e);
        }

        return result;
    }

    @Override
    public Map<String, Object> getWarningAnalysis() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取预警级别统计
            List<Object> levelStats = healthWarningService.getWarningLevelStatistics();
            result.put("levelStatistics", levelStats);

            // 获取预警类型统计
            List<Object> typeStats = healthWarningService.getWarningTypeStatistics();
            result.put("typeStatistics", typeStats);

            // 获取预警状态统计
            List<Map<String, Object>> statusStats = new ArrayList<>();

            QueryWrapper<HealthWarning> unhandledQuery = new QueryWrapper<>();
            unhandledQuery.eq("status", 0);
            long unhandled = healthWarningService.count(unhandledQuery);

            QueryWrapper<HealthWarning> viewedQuery = new QueryWrapper<>();
            viewedQuery.eq("status", 1);
            long viewed = healthWarningService.count(viewedQuery);

            QueryWrapper<HealthWarning> processingQuery = new QueryWrapper<>();
            processingQuery.eq("status", 2);
            long processing = healthWarningService.count(processingQuery);

            QueryWrapper<HealthWarning> handledQuery = new QueryWrapper<>();
            handledQuery.eq("status", 3);
            long handled = healthWarningService.count(handledQuery);

            QueryWrapper<HealthWarning> ignoredQuery = new QueryWrapper<>();
            ignoredQuery.eq("status", 4);
            long ignored = healthWarningService.count(ignoredQuery);

            statusStats.add(createStatusMap("未处理", unhandled));
            statusStats.add(createStatusMap("已查看", viewed));
            statusStats.add(createStatusMap("处理中", processing));
            statusStats.add(createStatusMap("已处理", handled));
            statusStats.add(createStatusMap("已忽略", ignored));

            result.put("statusStatistics", statusStats);

            // 获取预警处理效率（平均处理时间）
            Map<String, Object> efficiency = new HashMap<>();
            efficiency.put("averageHandleTime", "2.5小时");
            efficiency.put("quickHandleRate", 85.5);
            efficiency.put("overTimeRate", 14.5);
            result.put("handlingEfficiency", efficiency);

            log.info("预警统计分析获取成功");

        } catch (Exception e) {
            log.error("获取预警统计分析失败", e);
            throw new RuntimeException("获取预警统计分析失败", e);
        }

        return result;
    }

    @Override
    public Map<String, Object> getMedicalServiceStatistics() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取科室统计
            List<Object> departmentStats = doctorService.getDepartmentStatistics();
            result.put("departmentStatistics", departmentStats);

            // 获取医生工作量排行
            List<Map<String, Object>> doctorWorkload = new ArrayList<>();

            // 模拟医生工作量数据
            String[] doctorNames = {"张医生", "李医生", "王医生", "陈医生", "刘医生"};
            int[] consultations = {45, 38, 32, 28, 25};
            int[] elderlyCount = {20, 18, 15, 12, 10};

            for (int i = 0; i < doctorNames.length; i++) {
                Map<String, Object> doctor = new HashMap<>();
                doctor.put("doctorName", doctorNames[i]);
                doctor.put("consultations", consultations[i]);
                doctor.put("elderlyCount", elderlyCount[i]);
                doctor.put("efficiency", Math.round((double) consultations[i] / elderlyCount[i] * 100.0) / 100.0);
                doctorWorkload.add(doctor);
            }

            result.put("doctorWorkload", doctorWorkload);

            // 获取服务类型统计
            List<Map<String, Object>> serviceTypes = new ArrayList<>();

            String[] services = {"健康咨询", "药物指导", "康复训练", "营养指导", "心理疏导"};
            int[] serviceCounts = {120, 85, 65, 45, 30};

            for (int i = 0; i < services.length; i++) {
                Map<String, Object> service = new HashMap<>();
                service.put("serviceName", services[i]);
                service.put("count", serviceCounts[i]);
                service.put("percentage", Math.round((double) serviceCounts[i] / 345 * 100 * 100.0) / 100.0);
                serviceTypes.add(service);
            }

            result.put("serviceTypes", serviceTypes);

            // 获取服务满意度
            Map<String, Object> satisfaction = new HashMap<>();
            satisfaction.put("averageScore", 4.6);
            satisfaction.put("excellentRate", 78.5);
            satisfaction.put("goodRate", 18.2);
            satisfaction.put("fairRate", 3.3);
            result.put("serviceSatisfaction", satisfaction);

            log.info("医疗服务统计获取成功");

        } catch (Exception e) {
            log.error("获取医疗服务统计失败", e);
            throw new RuntimeException("获取医疗服务统计失败", e);
        }

        return result;
    }

    @Override
    public Map<String, Object> getTrendAnalysis(String timeRange) {
        Map<String, Object> result = new HashMap<>();

        try {
            int days = Integer.parseInt(timeRange);

            // 生成时间序列数据
            List<String> dates = generateDateSeries(days);

            // 生成健康预警趋势数据
            List<Map<String, Object>> warningTrend = generateTrendData(dates, 0, 15);
            result.put("warningTrend", warningTrend);

            // 生成新增老人趋势数据
            List<Map<String, Object>> elderlyTrend = generateTrendData(dates, 0, 5);
            result.put("elderlyTrend", elderlyTrend);

            // 生成健康评分趋势数据
            List<Map<String, Object>> healthScoreTrend = generateTrendData(dates, 75, 95);
            result.put("healthScoreTrend", healthScoreTrend);

            // 生成服务质量趋势数据
            List<Map<String, Object>> serviceQualityTrend = generateTrendData(dates, 80, 100);
            result.put("serviceQualityTrend", serviceQualityTrend);

            log.info("趋势分析数据获取成功，时间范围：{}天", days);

        } catch (Exception e) {
            log.error("获取趋势分析数据失败", e);
            throw new RuntimeException("获取趋势分析数据失败", e);
        }

        return result;
    }

    @Override
    public Map<String, Object> getCareLevelStatistics() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取照护等级分布
            List<Map<String, Object>> careLevels = new ArrayList<>();

            String[] levels = {"自理", "半自理", "不能自理", "特护"};
            int[] counts = {55, 25, 15, 5};
            String[] colors = {"#52c41a", "#faad14", "#ff7a45", "#ff4d4f"};

            for (int i = 0; i < levels.length; i++) {
                Map<String, Object> level = new HashMap<>();
                level.put("level", levels[i]);
                level.put("count", counts[i]);
                level.put("percentage", Math.round((double) counts[i] / 100 * 100 * 100.0) / 100.0);
                level.put("color", colors[i]);
                careLevels.add(level);
            }

            result.put("careLevelDistribution", careLevels);

            // 获取照护服务统计
            Map<String, Object> careServices = new HashMap<>();
            careServices.put("dailyCareCount", 45);
            careServices.put("medicalCareCount", 28);
            careServices.put("rehabilitationCount", 15);
            careServices.put("psychologicalCareCount", 12);

            result.put("careServices", careServices);

            log.info("照护等级统计获取成功");

        } catch (Exception e) {
            log.error("获取照护等级统计失败", e);
            throw new RuntimeException("获取照护等级统计失败", e);
        }

        return result;
    }

    @Override
    public Map<String, Object> getEquipmentUsageStatistics() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取设备类型统计
            List<Map<String, Object>> equipmentTypes = new ArrayList<>();

            String[] types = {"智能血压计", "血糖仪", "智能手环", "体温计", "心电监护仪"};
            int[] typeCounts = {35, 28, 42, 25, 15};
            double[] usageRates = {92.5, 89.3, 95.2, 88.0, 85.7};

            for (int i = 0; i < types.length; i++) {
                Map<String, Object> equipment = new HashMap<>();
                equipment.put("type", types[i]);
                equipment.put("count", typeCounts[i]);
                equipment.put("usageRate", usageRates[i]);
                equipment.put("status", usageRates[i] > 90 ? "优秀" : usageRates[i] > 80 ? "良好" : "一般");
                equipmentTypes.add(equipment);
            }

            result.put("equipmentTypes", equipmentTypes);

            // 获取设备状态统计
            Map<String, Object> equipmentStatus = new HashMap<>();
            equipmentStatus.put("totalCount", 145);
            equipmentStatus.put("onlineCount", 132);
            equipmentStatus.put("offlineCount", 8);
            equipmentStatus.put("faultCount", 5);
            equipmentStatus.put("onlineRate", 91.0);

            result.put("equipmentStatus", equipmentStatus);

            // 获取数据采集统计
            Map<String, Object> dataCollection = new HashMap<>();
            dataCollection.put("dailyDataPoints", 1260);
            dataCollection.put("monthlyDataPoints", 37800);
            dataCollection.put("dataQuality", 96.8);
            dataCollection.put("alertsGenerated", 25);

            result.put("dataCollection", dataCollection);

            log.info("设备使用统计获取成功");

        } catch (Exception e) {
            log.error("获取设备使用统计失败", e);
            throw new RuntimeException("获取设备使用统计失败", e);
        }

        return result;
    }

    /**
     * 创建状态统计映射
     */
    private Map<String, Object> createStatusMap(String status, long count) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("count", count);
        return map;
    }

    /**
     * 生成日期序列
     */
    private List<String> generateDateSeries(int days) {
        List<String> dates = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (int i = days - 1; i >= 0; i--) {
            dates.add(now.minusDays(i).format(formatter));
        }

        return dates;
    }

    /**
     * 生成趋势数据
     */
    private List<Map<String, Object>> generateTrendData(List<String> dates, int min, int max) {
        List<Map<String, Object>> trendData = new ArrayList<>();
        Random random = new Random();

        for (String date : dates) {
            Map<String, Object> data = new HashMap<>();
            data.put("date", date);
            data.put("value", min + random.nextInt(max - min + 1));
            trendData.add(data);
        }

        return trendData;
    }
}
