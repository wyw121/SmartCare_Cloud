package com.smartcare.cloud.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartcare.cloud.entity.DoctorElderlyRelation;

/**
 * 医生-老人关联关系Mapper接口
 * 
 * 用于医生数据权限隔离的核心Mapper
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Mapper
public interface DoctorElderlyRelationMapper extends BaseMapper<DoctorElderlyRelation> {
    
    /**
     * 查询医生负责的所有老人ID列表
     * 
     * @param doctorId 医生ID
     * @return 老人ID列表
     */
    List<Long> selectElderlyIdsByDoctorId(@Param("doctorId") Long doctorId);
    
    /**
     * 检查医生是否有权限访问指定老人
     * 
     * @param doctorId 医生ID
     * @param elderlyId 老人ID
     * @return 是否有权限 (有效关联记录数)
     */
    int checkDoctorElderlyAccess(@Param("doctorId") Long doctorId, 
                                   @Param("elderlyId") Long elderlyId);
    
    /**
     * 查询老人的主治医生ID
     * 
     * @param elderlyId 老人ID
     * @return 主治医生ID
     */
    Long selectPrimaryDoctorId(@Param("elderlyId") Long elderlyId);
}
