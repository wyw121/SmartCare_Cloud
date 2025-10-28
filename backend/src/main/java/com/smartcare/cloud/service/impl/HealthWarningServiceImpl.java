package com.smartcare.cloud.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smartcare.cloud.dto.HealthWarningPageDTO;
import com.smartcare.cloud.entity.HealthWarning;
import com.smartcare.cloud.event.HealthWarningCreatedEvent;
import com.smartcare.cloud.mapper.HealthWarningMapper;
import com.smartcare.cloud.service.HealthWarningService;

/**
 * 健康预警服务实现类
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Service
public class HealthWarningServiceImpl extends ServiceImpl<HealthWarningMapper, HealthWarning> implements HealthWarningService {

    private static final Logger log = LoggerFactory.getLogger(HealthWarningServiceImpl.class);

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public PageInfo<HealthWarning> getPageList(HealthWarningPageDTO dto) {
        log.info("分页查询健康预警，参数：{}", dto);

        // 设置分页参数
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<HealthWarning> queryWrapper = new LambdaQueryWrapper<>();

        // 老人ID
        if (dto.getElderlyId() != null) {
            queryWrapper.eq(HealthWarning::getElderlyId, dto.getElderlyId());
        }

        // 老人姓名模糊查询
        if (StringUtils.hasText(dto.getElderlyName())) {
            queryWrapper.like(HealthWarning::getElderlyName, dto.getElderlyName());
        }

        // 预警类型（支持多选）
        if (StringUtils.hasText(dto.getWarningType())) {
            String[] warningTypes = dto.getWarningType().split(",");
            if (warningTypes.length == 1) {
                queryWrapper.eq(HealthWarning::getWarningType, warningTypes[0].trim());
            } else {
                queryWrapper.in(HealthWarning::getWarningType, (Object[]) warningTypes);
            }
        }

        // 预警级别（支持多选）
        if (StringUtils.hasText(dto.getWarningLevel())) {
            String[] warningLevels = dto.getWarningLevel().split(",");
            if (warningLevels.length == 1) {
                try {
                    Integer level = Integer.parseInt(warningLevels[0].trim());
                    queryWrapper.eq(HealthWarning::getWarningLevel, level);
                } catch (NumberFormatException e) {
                    log.warn("预警级别格式错误: {}", warningLevels[0]);
                }
            } else {
                Integer[] levels = new Integer[warningLevels.length];
                for (int i = 0; i < warningLevels.length; i++) {
                    try {
                        levels[i] = Integer.parseInt(warningLevels[i].trim());
                    } catch (NumberFormatException e) {
                        log.warn("预警级别格式错误: {}", warningLevels[i]);
                    }
                }
                queryWrapper.in(HealthWarning::getWarningLevel, (Object[]) levels);
            }
        }

        // 状态（支持多选）
        if (StringUtils.hasText(dto.getStatus())) {
            String[] statuses = dto.getStatus().split(",");
            if (statuses.length == 1) {
                try {
                    Integer status = Integer.parseInt(statuses[0].trim());
                    queryWrapper.eq(HealthWarning::getStatus, status);
                } catch (NumberFormatException e) {
                    log.warn("状态格式错误: {}", statuses[0]);
                }
            } else {
                Integer[] statusArray = new Integer[statuses.length];
                for (int i = 0; i < statuses.length; i++) {
                    try {
                        statusArray[i] = Integer.parseInt(statuses[i].trim());
                    } catch (NumberFormatException e) {
                        log.warn("状态格式错误: {}", statuses[i]);
                    }
                }
                queryWrapper.in(HealthWarning::getStatus, (Object[]) statusArray);
            }
        }

        // 处理人ID
        if (dto.getHandlerId() != null) {
            queryWrapper.eq(HealthWarning::getHandlerId, dto.getHandlerId());
        }

        // 处理人姓名
        if (StringUtils.hasText(dto.getHandlerName())) {
            queryWrapper.like(HealthWarning::getHandlerName, dto.getHandlerName());
        }

        // 时间范围
        if (dto.getStartTime() != null) {
            queryWrapper.ge(HealthWarning::getTriggerTime, dto.getStartTime());
        }
        if (dto.getEndTime() != null) {
            queryWrapper.le(HealthWarning::getTriggerTime, dto.getEndTime());
        }

        // 数据来源
        if (StringUtils.hasText(dto.getDataSource())) {
            queryWrapper.eq(HealthWarning::getDataSource, dto.getDataSource());
        }

        // 按创建时间倒序
        queryWrapper.orderByDesc(HealthWarning::getTriggerTime);

        // 查询数据
        List<HealthWarning> list = this.list(queryWrapper);

        return new PageInfo<>(list);
    }

    @Override
    public List<HealthWarning> getByElderlyId(Long elderlyId) {
        LambdaQueryWrapper<HealthWarning> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HealthWarning::getElderlyId, elderlyId);
        queryWrapper.orderByDesc(HealthWarning::getTriggerTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<HealthWarning> getByStatus(Integer status) {
        LambdaQueryWrapper<HealthWarning> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HealthWarning::getStatus, status);
        queryWrapper.orderByDesc(HealthWarning::getTriggerTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<HealthWarning> getByWarningLevel(Integer warningLevel) {
        LambdaQueryWrapper<HealthWarning> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HealthWarning::getWarningLevel, warningLevel);
        queryWrapper.orderByDesc(HealthWarning::getTriggerTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<HealthWarning> getByWarningType(String warningType) {
        LambdaQueryWrapper<HealthWarning> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HealthWarning::getWarningType, warningType);
        queryWrapper.orderByDesc(HealthWarning::getTriggerTime);
        return this.list(queryWrapper);
    }

    @Override
    public boolean handleWarning(Long id, Long handlerId, String handlerName, String handleRemark) {
        HealthWarning healthWarning = this.getById(id);
        if (healthWarning == null) {
            return false;
        }

        healthWarning.setStatus(3); // 已处理
        healthWarning.setHandlerId(handlerId);
        healthWarning.setHandlerName(handlerName);
        healthWarning.setHandleRemark(handleRemark);
        healthWarning.setHandleTime(LocalDateTime.now());

        return this.updateById(healthWarning);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        HealthWarning healthWarning = this.getById(id);
        if (healthWarning == null) {
            return false;
        }

        healthWarning.setStatus(status);
        if (status == 1) { // 已查看
            // 可以记录查看时间等
        }

        return this.updateById(healthWarning);
    }

    @Override
    public boolean deleteBatch(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public long getUnhandledCount() {
        LambdaQueryWrapper<HealthWarning> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HealthWarning::getStatus, 0); // 未处理
        return this.count(queryWrapper);
    }

    @Override
    public List<Object> getWarningLevelStatistics() {
        // 使用原生SQL查询统计数据
        QueryWrapper<HealthWarning> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("warning_level as level, count(*) as count")
                .groupBy("warning_level")
                .orderBy(true, true, "warning_level");

        return this.baseMapper.selectMaps(queryWrapper).stream()
                .map(map -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("level", map.get("level"));
                    result.put("count", map.get("count"));
                    return result;
                })
                .map(Object.class::cast)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Object> getWarningTypeStatistics() {
        // 使用原生SQL查询统计数据
        QueryWrapper<HealthWarning> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("warning_type as type, count(*) as count")
                .groupBy("warning_type")
                .orderBy(false, false, "count");

        return this.baseMapper.selectMaps(queryWrapper).stream()
                .map(map -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("type", map.get("type"));
                    result.put("count", map.get("count"));
                    return result;
                })
                .map(Object.class::cast)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean createWarning(Long elderlyId, String elderlyName, String warningType,
            Integer warningLevel, String title, String content,
            String triggerData, String dataSource) {
        HealthWarning healthWarning = new HealthWarning();
        healthWarning.setElderlyId(elderlyId);
        healthWarning.setElderlyName(elderlyName);
        healthWarning.setWarningType(warningType);
        healthWarning.setWarningLevel(warningLevel);
        healthWarning.setTitle(title);
        healthWarning.setContent(content);
        healthWarning.setTriggerData(triggerData);
        healthWarning.setDataSource(dataSource);
        healthWarning.setStatus(0); // 未处理
        healthWarning.setTriggerTime(LocalDateTime.now());

        return this.save(healthWarning);
    }

    /**
     * 根据老人ID列表分页查询健康预警列表（家属专用）
     *
     * @param dto 查询条件
     * @param elderlyIds 允许查询的老人ID列表
     * @return 分页结果
     */
    @Override
    public PageInfo<HealthWarning> getPageListByElderlyIds(HealthWarningPageDTO dto, List<Long> elderlyIds) {
        log.info("根据老人ID列表分页查询健康预警，参数：{}，老人IDs：{}", dto, elderlyIds);

        // 设置分页参数
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<HealthWarning> queryWrapper = new LambdaQueryWrapper<>();

        // 限制查询范围为指定的老人ID列表
        if (elderlyIds != null && !elderlyIds.isEmpty()) {
            queryWrapper.in(HealthWarning::getElderlyId, elderlyIds);
        } else {
            // 如果没有允许的老人ID，返回空结果
            return new PageInfo<>(new ArrayList<>());
        }

        // 老人姓名模糊查询
        if (StringUtils.hasText(dto.getElderlyName())) {
            queryWrapper.like(HealthWarning::getElderlyName, dto.getElderlyName());
        }

        // 预警类型（支持多选）
        if (StringUtils.hasText(dto.getWarningType())) {
            String[] warningTypes = dto.getWarningType().split(",");
            if (warningTypes.length == 1) {
                queryWrapper.eq(HealthWarning::getWarningType, warningTypes[0].trim());
            } else {
                queryWrapper.in(HealthWarning::getWarningType, (Object[]) warningTypes);
            }
        }

        // 预警级别（支持多选）
        if (StringUtils.hasText(dto.getWarningLevel())) {
            String[] warningLevels = dto.getWarningLevel().split(",");
            if (warningLevels.length == 1) {
                try {
                    Integer level = Integer.parseInt(warningLevels[0].trim());
                    queryWrapper.eq(HealthWarning::getWarningLevel, level);
                } catch (NumberFormatException e) {
                    log.warn("预警级别格式错误: {}", warningLevels[0]);
                }
            } else {
                Integer[] levels = new Integer[warningLevels.length];
                for (int i = 0; i < warningLevels.length; i++) {
                    try {
                        levels[i] = Integer.parseInt(warningLevels[i].trim());
                    } catch (NumberFormatException e) {
                        log.warn("预警级别格式错误: {}", warningLevels[i]);
                    }
                }
                queryWrapper.in(HealthWarning::getWarningLevel, (Object[]) levels);
            }
        }

        // 状态（支持多选）
        if (StringUtils.hasText(dto.getStatus())) {
            String[] statuses = dto.getStatus().split(",");
            if (statuses.length == 1) {
                try {
                    Integer status = Integer.parseInt(statuses[0].trim());
                    queryWrapper.eq(HealthWarning::getStatus, status);
                } catch (NumberFormatException e) {
                    log.warn("状态格式错误: {}", statuses[0]);
                }
            } else {
                Integer[] statusArray = new Integer[statuses.length];
                for (int i = 0; i < statuses.length; i++) {
                    try {
                        statusArray[i] = Integer.parseInt(statuses[i].trim());
                    } catch (NumberFormatException e) {
                        log.warn("状态格式错误: {}", statuses[i]);
                    }
                }
                queryWrapper.in(HealthWarning::getStatus, statusArray);
            }
        }

        // 时间范围查询
        if (dto.getStartTime() != null) {
            queryWrapper.ge(HealthWarning::getTriggerTime, dto.getStartTime());
        }
        if (dto.getEndTime() != null) {
            queryWrapper.le(HealthWarning::getTriggerTime, dto.getEndTime());
        }

        // 按创建时间倒序
        queryWrapper.orderByDesc(HealthWarning::getTriggerTime);

        // 查询数据
        List<HealthWarning> list = this.list(queryWrapper);

        return new PageInfo<>(list);
    }

    /**
     * 根据老人ID列表获取健康预警统计数据（家属专用）
     *
     * @param elderlyIds 老人ID列表
     * @return 统计结果
     */
    @Override
    public Map<String, Object> getWarningStatisticsByElderlyIds(List<Long> elderlyIds) {
        log.info("根据老人ID列表获取健康预警统计，老人IDs：{}", elderlyIds);

        Map<String, Object> result = new HashMap<>();

        if (elderlyIds == null || elderlyIds.isEmpty()) {
            // 如果没有关联老人，返回全零统计
            result.put("urgent", 0);
            result.put("high", 0);
            result.put("medium", 0);
            result.put("low", 0);
            result.put("total", 0);
            return result;
        }

        // 按级别统计
        LambdaQueryWrapper<HealthWarning> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(HealthWarning::getElderlyId, elderlyIds);

        // 统计各级别数量
        long urgent = this.count(queryWrapper.clone().eq(HealthWarning::getWarningLevel, 4));
        long high = this.count(queryWrapper.clone().eq(HealthWarning::getWarningLevel, 3));
        long medium = this.count(queryWrapper.clone().eq(HealthWarning::getWarningLevel, 2));
        long low = this.count(queryWrapper.clone().eq(HealthWarning::getWarningLevel, 1));
        long total = urgent + high + medium + low;

        result.put("urgent", urgent);
        result.put("high", high);
        result.put("medium", medium);
        result.put("low", low);
        result.put("total", total);

        return result;
    }

    /**
     * 创建健康预警并发布事件
     * 
     * 此方法包装了MyBatis-Plus的save()方法,
     * 在保存成功后自动发布HealthWarningCreatedEvent事件,
     * 触发后续的通知、日志、统计等异步处理
     * 
     * @param healthWarning 健康预警对象
     * @param source 预警来源(system/device/manual)
     * @return 保存结果
     */
    public boolean createWarningWithEvent(HealthWarning healthWarning, String source) {
        try {
            // 保存预警记录
            boolean success = this.save(healthWarning);
            
            if (success) {
                // 判断是否紧急(级别>=3为高/紧急)
                boolean urgent = healthWarning.getWarningLevel() != null 
                    && healthWarning.getWarningLevel() >= 3;
                
                // 发布健康预警创建事件
                HealthWarningCreatedEvent event = new HealthWarningCreatedEvent(
                    this, 
                    healthWarning, 
                    source, 
                    urgent
                );
                eventPublisher.publishEvent(event);
                
                log.info("健康预警创建成功并发布事件,ID:{},老人ID:{},级别:{},来源:{}", 
                    healthWarning.getId(),
                    healthWarning.getElderlyId(),
                    healthWarning.getWarningLevel(),
                    source);
            }
            
            return success;
            
        } catch (Exception e) {
            log.error("创建健康预警失败", e);
            return false;
        }
    }
}
