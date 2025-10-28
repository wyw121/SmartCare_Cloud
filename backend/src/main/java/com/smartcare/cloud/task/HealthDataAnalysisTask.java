package com.smartcare.cloud.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.service.ElderlyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 健康数据分析定时任务
 * 定期分析老人健康数据,生成统计报告
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HealthDataAnalysisTask {

    private final ElderlyService elderlyService;

    /**
     * 每日健康数据统计
     * 每天凌晨1点执行
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void dailyHealthStatistics() {
        try {
            log.info("开始执行每日健康数据统计任务...");

            // 统计老人总数
            long totalElderly = elderlyService.count();

            // 统计各健康状态人数
            long healthyCount = countByHealthStatus("健康");
            long subHealthCount = countByHealthStatus("亚健康");
            long chronicCount = countByHealthStatus("慢性病");
            long illCount = countByHealthStatus("疾病");

            log.info("每日健康统计完成 - 总人数:{}, 健康:{}, 亚健康:{}, 慢性病:{}, 疾病:{}",
                    totalElderly, healthyCount, subHealthCount, chronicCount, illCount);

            // TODO: 将统计结果写入Redis或数据库,供报表使用
            // redisTemplate.opsForValue().set("daily:health:stats:" + LocalDate.now(), statsMap);

        } catch (Exception e) {
            log.error("每日健康数据统计任务执行失败", e);
        }
    }

    /**
     * 每周健康趋势分析
     * 每周一凌晨2点执行
     */
    @Scheduled(cron = "0 0 2 ? * MON")
    public void weeklyHealthTrendAnalysis() {
        try {
            log.info("开始执行每周健康趋势分析任务...");

            // 分析过去7天的健康数据变化
            LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);

            // 查询近一周新增老人
            QueryWrapper<Elderly> newElderlyQuery = new QueryWrapper<>();
            newElderlyQuery.ge("created_time", weekAgo);
            long newElderlyCount = elderlyService.count(newElderlyQuery);

            log.info("每周健康趋势分析完成 - 新增老人:{}", newElderlyCount);

            // TODO: 生成周报,发送给管理员
            // emailService.sendWeeklyReport(report);

        } catch (Exception e) {
            log.error("每周健康趋势分析任务执行失败", e);
        }
    }

    /**
     * 每月健康评估报告
     * 每月1号凌晨3点执行
     */
    @Scheduled(cron = "0 0 3 1 * ?")
    public void monthlyHealthAssessmentReport() {
        try {
            log.info("开始生成每月健康评估报告...");

            // 统计上个月的数据
            LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);

            // 查询需要健康评估的老人(假设每月评估一次)
            QueryWrapper<Elderly> query = new QueryWrapper<>();
            query.isNotNull("id");  // 所有老人
            List<Elderly> elderlyList = elderlyService.list(query);

            log.info("每月健康评估报告生成完成 - 评估老人数:{}", elderlyList.size());

            // TODO: 为每位老人生成健康评估报告
            // for (Elderly elderly : elderlyList) {
            //     assessmentService.generateMonthlyReport(elderly.getId());
            // }

        } catch (Exception e) {
            log.error("每月健康评估报告生成任务执行失败", e);
        }
    }

    /**
     * 检查需要关注的老人
     * 每天早上8点执行
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkElderlyNeedAttention() {
        try {
            log.info("开始检查需要关注的老人...");

            // 检查护理等级高的老人(等级3和4需要重点关注)
            QueryWrapper<Elderly> query = new QueryWrapper<>();
            query.ge("care_level", 3);  // 不能自理、特级护理
            List<Elderly> needAttention = elderlyService.list(query);

            if (!needAttention.isEmpty()) {
                log.warn("当前有{}位老人需要重点关注(护理等级>=3)", needAttention.size());

                // TODO: 发送提醒给护理人员
                // notificationService.notifyCargivers(needAttention);
            } else {
                log.info("暂无需要重点关注的老人");
            }

        } catch (Exception e) {
            log.error("检查需要关注老人任务执行失败", e);
        }
    }

    /**
     * 按健康状态统计人数
     */
    private long countByHealthStatus(String healthStatus) {
        QueryWrapper<Elderly> query = new QueryWrapper<>();
        query.eq("health_status", healthStatus);
        return elderlyService.count(query);
    }
}
