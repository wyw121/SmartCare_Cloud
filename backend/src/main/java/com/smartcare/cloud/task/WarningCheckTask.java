package com.smartcare.cloud.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcare.cloud.entity.HealthWarning;
import com.smartcare.cloud.service.HealthWarningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预警检查定时任务
 * 定期扫描和处理健康预警
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WarningCheckTask {

    private final HealthWarningService healthWarningService;

    /**
     * 检查未处理的预警
     * 每5分钟执行一次
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void checkUnhandledWarnings() {
        try {
            log.info("开始检查未处理的健康预警...");

            // 查询未处理且超过30分钟的预警
            LocalDateTime threshold = LocalDateTime.now().minusMinutes(30);
            QueryWrapper<HealthWarning> query = new QueryWrapper<>();
            query.eq("status", 0)  // 未处理
                    .lt("created_time", threshold)
                    .orderByAsc("level")  // 按级别升序,高级别优先
                    .last("LIMIT 100");

            List<HealthWarning> unhandledWarnings = healthWarningService.list(query);

            if (unhandledWarnings.isEmpty()) {
                log.info("未发现超时未处理的预警");
            } else {
                log.warn("发现{}条超时未处理的预警,需要处理", unhandledWarnings.size());

                // TODO: 这里可以发送提醒通知给管理员或自动分配处理人员
                for (HealthWarning warning : unhandledWarnings) {
                    log.warn("超时预警: ID={}, 老人ID={}, 级别={}, 创建时间={}",
                            warning.getId(),
                            warning.getElderlyId(),
                            warning.getLevel(),
                            warning.getCreatedTime());
                }
            }

        } catch (Exception e) {
            log.error("检查未处理预警任务执行失败", e);
        }
    }

    /**
     * 检查高级别预警的处理进度
     * 每10分钟执行一次
     */
    @Scheduled(cron = "0 */10 * * * ?")
    public void checkHighLevelWarnings() {
        try {
            log.info("开始检查高级别预警处理进度...");

            // 查询级别>=3(高和紧急)且状态为处理中的预警
            QueryWrapper<HealthWarning> query = new QueryWrapper<>();
            query.ge("level", 3)  // 高级别及以上
                    .eq("status", 2)  // 处理中
                    .orderByDesc("level")
                    .orderByAsc("created_time")
                    .last("LIMIT 50");

            List<HealthWarning> highLevelWarnings = healthWarningService.list(query);

            if (!highLevelWarnings.isEmpty()) {
                log.info("当前有{}条高级别预警正在处理中", highLevelWarnings.size());

                // TODO: 检查处理时长,超过阈值发送催办通知
                LocalDateTime urgentThreshold = LocalDateTime.now().minusHours(2);
                for (HealthWarning warning : highLevelWarnings) {
                    if (warning.getCreatedTime().isBefore(urgentThreshold)) {
                        log.warn("高级别预警处理超时: ID={}, 老人ID={}, 级别={}, 已处理时长>2小时",
                                warning.getId(),
                                warning.getElderlyId(),
                                warning.getLevel());
                    }
                }
            }

        } catch (Exception e) {
            log.error("检查高级别预警任务执行失败", e);
        }
    }

    /**
     * 清理过期的已处理预警
     * 每天凌晨3点执行
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanExpiredWarnings() {
        try {
            log.info("开始清理过期的已处理预警...");

            // 删除30天前的已处理预警(status=3或status=4)
            LocalDateTime expireDate = LocalDateTime.now().minusDays(30);
            QueryWrapper<HealthWarning> query = new QueryWrapper<>();
            query.in("status", 3, 4)  // 已处理或已忽略
                    .lt("updated_time", expireDate);

            long count = healthWarningService.count(query);
            if (count > 0) {
                // 注意: 实际项目中建议使用逻辑删除或归档,而不是物理删除
                log.info("发现{}条过期预警记录,建议归档", count);
                // healthWarningService.remove(query);  // 实际项目中应归档而非删除
            } else {
                log.info("未发现需要清理的过期预警");
            }

        } catch (Exception e) {
            log.error("清理过期预警任务执行失败", e);
        }
    }
}
