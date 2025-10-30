package com.smartcare.cloud.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartcare.cloud.entity.DoctorElderlyRelation;

/**
 * 医生-老人关联关系服务接口
 * 
 * 核心功能: 实现医生数据权限隔离
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
public interface DoctorElderlyRelationService extends IService<DoctorElderlyRelation> {

    /**
     * 获取医生负责的老人ID列表
     * 用于数据权限过滤
     * 
     * @param doctorId 医生ID
     * @return 老人ID列表
     */
    List<Long> getElderlyIdsByDoctorId(Long doctorId);

    /**
     * 检查医生是否有权限访问指定老人
     * 
     * @param doctorId 医生ID
     * @param elderlyId 老人ID
     * @return true-有权限, false-无权限
     */
    boolean checkDoctorElderlyAccess(Long doctorId, Long elderlyId);

    /**
     * 获取老人的主治医生ID
     * 
     * @param elderlyId 老人ID
     * @return 主治医生ID
     */
    Long getPrimaryDoctorId(Long elderlyId);

    /**
     * 为医生分配老人
     * 
     * @param doctorId 医生ID
     * @param elderlyIds 老人ID列表
     * @param relationshipType 关系类型 (primary/assistant/consultant)
     * @return 分配成功的数量
     */
    int assignElderlyToDoctor(Long doctorId, List<Long> elderlyIds, String relationshipType);

    /**
     * 解除医生与老人的关联
     * 
     * @param doctorId 医生ID
     * @param elderlyId 老人ID
     * @return 是否成功
     */
    boolean removeElderlyFromDoctor(Long doctorId, Long elderlyId);

    /**
     * 转移老人的主治医生
     * 
     * @param elderlyId 老人ID
     * @param oldDoctorId 原医生ID
     * @param newDoctorId 新医生ID
     * @return 是否成功
     */
    boolean transferPrimaryDoctor(Long elderlyId, Long oldDoctorId, Long newDoctorId);
}
