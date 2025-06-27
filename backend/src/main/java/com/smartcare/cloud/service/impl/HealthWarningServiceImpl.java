package com.smartcare.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smartcare.cloud.dto.HealthWarningPageDTO;
import com.smartcare.cloud.entity.HealthWarning;
import com.smartcare.cloud.mapper.HealthWarningMapper;
import com.smartcare.cloud.service.HealthWarningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 健康预警服务实现类
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Service
public class HealthWarningServiceImpl extends ServiceImpl<HealthWarningMapper, HealthWarning> implements HealthWarningService {

    private static final Logger log = LoggerFactory.getLogger(HealthWarningServiceImpl.class);

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
        
        // 预警类型
        if (StringUtils.hasText(dto.getWarningType())) {
            queryWrapper.eq(HealthWarning::getWarningType, dto.getWarningType());
        }
        
        // 预警级别
        if (dto.getWarningLevel() != null) {
            queryWrapper.eq(HealthWarning::getWarningLevel, dto.getWarningLevel());
        }
        
        // 状态
        if (dto.getStatus() != null) {
            queryWrapper.eq(HealthWarning::getStatus, dto.getStatus());
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
}
