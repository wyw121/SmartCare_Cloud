package com.smartcare.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcare.cloud.dto.ElderlyPageDTO;
import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.mapper.ElderlyMapper;
import com.smartcare.cloud.service.ElderlyService;
import com.smartcare.cloud.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 老人信息服务实现类
 * 
 * @author SmartCare Team
 * @date 2024-01-01
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ElderlyServiceImpl extends ServiceImpl<ElderlyMapper, Elderly> implements ElderlyService {

    @Override
    public ResponseResult<Page<Elderly>> getElderlyPage(ElderlyPageDTO pageDTO) {
        try {
            Page<Elderly> page = new Page<>(pageDTO.getCurrent(), pageDTO.getSize());
            QueryWrapper<Elderly> queryWrapper = new QueryWrapper<>();
            
            // 根据姓名模糊查询
            if (StringUtils.isNotBlank(pageDTO.getName())) {
                queryWrapper.like("name", pageDTO.getName());
            }
            
            // 根据身份证号查询
            if (StringUtils.isNotBlank(pageDTO.getIdCard())) {
                queryWrapper.like("id_card", pageDTO.getIdCard());
            }
            
            // 根据健康状态查询
            if (StringUtils.isNotBlank(pageDTO.getHealthStatus())) {
                queryWrapper.eq("health_status", pageDTO.getHealthStatus());
            }
            
            // 根据年龄范围查询
            if (pageDTO.getMinAge() != null) {
                queryWrapper.ge("age", pageDTO.getMinAge());
            }
            if (pageDTO.getMaxAge() != null) {
                queryWrapper.le("age", pageDTO.getMaxAge());
            }
            
            // 按创建时间倒序
            queryWrapper.orderByDesc("create_time");
            
            Page<Elderly> result = this.page(page, queryWrapper);
            return ResponseResult.success(result);
            
        } catch (Exception e) {
            log.error("分页查询老人信息失败", e);
            return ResponseResult.error("查询失败");
        }
    }

    @Override
    public ResponseResult<Elderly> getElderlyById(Long id) {
        try {
            Elderly elderly = this.getById(id);
            if (elderly == null) {
                return ResponseResult.error("老人信息不存在");
            }
            return ResponseResult.success(elderly);
        } catch (Exception e) {
            log.error("根据ID查询老人信息失败，ID: {}", id, e);
            return ResponseResult.error("查询失败");
        }
    }

    @Override
    public ResponseResult<Void> addElderly(Elderly elderly) {
        try {
            // 检查身份证号是否已存在
            QueryWrapper<Elderly> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_card", elderly.getIdCard());
            Elderly existingElderly = this.getOne(queryWrapper);
            if (existingElderly != null) {
                return ResponseResult.error("身份证号已存在");
            }
            
            elderly.setCreateTime(LocalDateTime.now());
            elderly.setUpdateTime(LocalDateTime.now());
            boolean success = this.save(elderly);
            
            if (success) {
                return ResponseResult.success();
            } else {
                return ResponseResult.error("添加失败");
            }
        } catch (Exception e) {
            log.error("新增老人信息失败", e);
            return ResponseResult.error("添加失败");
        }
    }

    @Override
    public ResponseResult<Void> updateElderly(Elderly elderly) {
        try {
            Elderly existingElderly = this.getById(elderly.getId());
            if (existingElderly == null) {
                return ResponseResult.error("老人信息不存在");
            }
            
            // 检查身份证号是否被其他人使用
            QueryWrapper<Elderly> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_card", elderly.getIdCard())
                       .ne("id", elderly.getId());
            Elderly duplicateElderly = this.getOne(queryWrapper);
            if (duplicateElderly != null) {
                return ResponseResult.error("身份证号已被其他人使用");
            }
            
            elderly.setUpdateTime(LocalDateTime.now());
            boolean success = this.updateById(elderly);
            
            if (success) {
                return ResponseResult.success();
            } else {
                return ResponseResult.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新老人信息失败，ID: {}", elderly.getId(), e);
            return ResponseResult.error("更新失败");
        }
    }

    @Override
    public ResponseResult<Void> deleteElderly(Long id) {
        try {
            Elderly elderly = this.getById(id);
            if (elderly == null) {
                return ResponseResult.error("老人信息不存在");
            }
            
            boolean success = this.removeById(id);
            if (success) {
                return ResponseResult.success();
            } else {
                return ResponseResult.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除老人信息失败，ID: {}", id, e);
            return ResponseResult.error("删除失败");
        }
    }

    @Override
    public ResponseResult<Void> batchDeleteElderly(List<Long> ids) {
        try {
            boolean success = this.removeByIds(ids);
            if (success) {
                return ResponseResult.success();
            } else {
                return ResponseResult.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除老人信息失败，IDs: {}", ids, e);
            return ResponseResult.error("批量删除失败");
        }
    }

    @Override
    public ResponseResult<List<Elderly>> getKeyElderlyList() {
        try {
            QueryWrapper<Elderly> queryWrapper = new QueryWrapper<>();
            // 查询重点关注的老人（健康状态为危险或需要特殊关注的）
            queryWrapper.in("health_status", "DANGER", "WARNING")
                       .orderByDesc("update_time")
                       .last("LIMIT 20"); // 限制返回20条
            
            List<Elderly> elderlyList = this.list(queryWrapper);
            return ResponseResult.success(elderlyList);
        } catch (Exception e) {
            log.error("获取重点关注老人列表失败", e);
            return ResponseResult.error("查询失败");
        }
    }

    @Override
    public ResponseResult<Object> getElderlyHealthStatistics() {
        try {
            QueryWrapper<Elderly> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("health_status, COUNT(*) as count")
                       .groupBy("health_status");
            
            List<Map<String, Object>> statistics = this.listMaps(queryWrapper);
            
            // 统计总数
            long totalCount = this.count();
            
            Map<String, Object> result = new HashMap<>();
            result.put("total", totalCount);
            result.put("healthStatusStats", statistics);
            
            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("获取老人健康状态统计失败", e);
            return ResponseResult.error("统计失败");
        }
    }
}
