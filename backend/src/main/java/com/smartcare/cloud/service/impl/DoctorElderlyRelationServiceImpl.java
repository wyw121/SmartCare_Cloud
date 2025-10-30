package com.smartcare.cloud.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcare.cloud.entity.DoctorElderlyRelation;
import com.smartcare.cloud.mapper.DoctorElderlyRelationMapper;
import com.smartcare.cloud.service.DoctorElderlyRelationService;

/**
 * 医生-老人关联关系服务实现类
 * 
 * 实现医生数据权限隔离的核心逻辑
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Service
public class DoctorElderlyRelationServiceImpl extends ServiceImpl<DoctorElderlyRelationMapper, DoctorElderlyRelation>
        implements DoctorElderlyRelationService {

    private static final Logger log = LoggerFactory.getLogger(DoctorElderlyRelationServiceImpl.class);

    @Autowired
    private DoctorElderlyRelationMapper doctorElderlyRelationMapper;

    @Override
    public List<Long> getElderlyIdsByDoctorId(Long doctorId) {
        if (doctorId == null) {
            log.warn("获取医生负责老人列表失败: doctorId为空");
            return new ArrayList<>();
        }
        
        List<Long> elderlyIds = doctorElderlyRelationMapper.selectElderlyIdsByDoctorId(doctorId);
        log.info("医生[{}]当前负责{}位老人", doctorId, elderlyIds.size());
        return elderlyIds;
    }

    @Override
    public boolean checkDoctorElderlyAccess(Long doctorId, Long elderlyId) {
        if (doctorId == null || elderlyId == null) {
            log.warn("权限检查失败: doctorId或elderlyId为空");
            return false;
        }
        
        int count = doctorElderlyRelationMapper.checkDoctorElderlyAccess(doctorId, elderlyId);
        boolean hasAccess = count > 0;
        
        if (!hasAccess) {
            log.warn("权限校验失败: 医生[{}]尝试访问非负责老人[{}]", doctorId, elderlyId);
        }
        
        return hasAccess;
    }

    @Override
    public Long getPrimaryDoctorId(Long elderlyId) {
        if (elderlyId == null) {
            log.warn("获取主治医生失败: elderlyId为空");
            return null;
        }
        
        Long primaryDoctorId = doctorElderlyRelationMapper.selectPrimaryDoctorId(elderlyId);
        
        if (primaryDoctorId == null) {
            log.warn("老人[{}]暂无主治医生", elderlyId);
        }
        
        return primaryDoctorId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int assignElderlyToDoctor(Long doctorId, List<Long> elderlyIds, String relationshipType) {
        if (doctorId == null || elderlyIds == null || elderlyIds.isEmpty()) {
            log.warn("分配老人失败: 参数为空");
            return 0;
        }
        
        // 默认关系类型为assistant
        if (relationshipType == null || relationshipType.trim().isEmpty()) {
            relationshipType = "assistant";
        }
        
        int successCount = 0;
        LocalDate now = LocalDate.now();
        
        for (Long elderlyId : elderlyIds) {
            try {
                // 检查是否已存在关联
                QueryWrapper<DoctorElderlyRelation> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("doctor_id", doctorId)
                           .eq("elderly_id", elderlyId)
                           .eq("status", 1)
                           .eq("is_deleted", 0);
                
                DoctorElderlyRelation existing = this.getOne(queryWrapper);
                
                if (existing != null) {
                    log.warn("医生[{}]与老人[{}]已存在关联关系", doctorId, elderlyId);
                    continue;
                }
                
                // 创建新关联
                DoctorElderlyRelation relation = new DoctorElderlyRelation();
                relation.setDoctorId(doctorId);
                relation.setElderlyId(elderlyId);
                relation.setRelationshipType(relationshipType);
                relation.setStartDate(now);
                relation.setStatus(1);
                relation.setIsDeleted(0);
                
                boolean saved = this.save(relation);
                
                if (saved) {
                    successCount++;
                    log.info("成功为医生[{}]分配老人[{}],关系类型:{}", doctorId, elderlyId, relationshipType);
                }
                
            } catch (Exception e) {
                log.error("分配老人[{}]给医生[{}]失败", elderlyId, doctorId, e);
            }
        }
        
        log.info("医生[{}]分配结果: 成功{}/总计{}", doctorId, successCount, elderlyIds.size());
        return successCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeElderlyFromDoctor(Long doctorId, Long elderlyId) {
        if (doctorId == null || elderlyId == null) {
            log.warn("解除关联失败: doctorId或elderlyId为空");
            return false;
        }
        
        QueryWrapper<DoctorElderlyRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("doctor_id", doctorId)
                   .eq("elderly_id", elderlyId)
                   .eq("status", 1)
                   .eq("is_deleted", 0);
        
        DoctorElderlyRelation relation = this.getOne(queryWrapper);
        
        if (relation == null) {
            log.warn("医生[{}]与老人[{}]无有效关联关系", doctorId, elderlyId);
            return false;
        }
        
        // 设置结束日期和状态
        relation.setEndDate(LocalDate.now());
        relation.setStatus(0);
        relation.setUpdateTime(LocalDateTime.now());
        
        boolean updated = this.updateById(relation);
        
        if (updated) {
            log.info("成功解除医生[{}]与老人[{}]的关联", doctorId, elderlyId);
        }
        
        return updated;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean transferPrimaryDoctor(Long elderlyId, Long oldDoctorId, Long newDoctorId) {
        if (elderlyId == null || oldDoctorId == null || newDoctorId == null) {
            log.warn("转移主治医生失败: 参数为空");
            return false;
        }
        
        if (oldDoctorId.equals(newDoctorId)) {
            log.warn("转移主治医生失败: 新旧医生ID相同");
            return false;
        }
        
        try {
            // 1. 结束旧医生的主治关系
            boolean removed = this.removeElderlyFromDoctor(oldDoctorId, elderlyId);
            
            if (!removed) {
                log.warn("结束旧主治医生关系失败");
                return false;
            }
            
            // 2. 创建新医生的主治关系
            DoctorElderlyRelation newRelation = new DoctorElderlyRelation();
            newRelation.setDoctorId(newDoctorId);
            newRelation.setElderlyId(elderlyId);
            newRelation.setRelationshipType("primary");
            newRelation.setStartDate(LocalDate.now());
            newRelation.setStatus(1);
            newRelation.setIsDeleted(0);
            
            boolean saved = this.save(newRelation);
            
            if (saved) {
                log.info("成功将老人[{}]的主治医生从[{}]转移到[{}]", elderlyId, oldDoctorId, newDoctorId);
            }
            
            return saved;
            
        } catch (Exception e) {
            log.error("转移主治医生失败: 老人[{}], 原医生[{}], 新医生[{}]", 
                     elderlyId, oldDoctorId, newDoctorId, e);
            throw e; // 抛出异常触发事务回滚
        }
    }
}
