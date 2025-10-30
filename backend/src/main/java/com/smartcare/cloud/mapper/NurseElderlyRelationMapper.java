package com.smartcare.cloud.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartcare.cloud.entity.NurseElderlyRelation;

/**
 * 护工-老人关联关系Mapper接口
 * 
 * 用于护工照护分配和工作负荷管理
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Mapper
public interface NurseElderlyRelationMapper extends BaseMapper<NurseElderlyRelation> {
    
    /**
     * 查询护工负责的所有老人ID列表
     * 
     * @param nurseId 护工ID
     * @return 老人ID列表
     */
    List<Long> selectElderlyIdsByNurseId(@Param("nurseId") Long nurseId);
    
    /**
     * 检查护工是否有权限访问指定老人
     * 
     * @param nurseId 护工ID
     * @param elderlyId 老人ID
     * @return 是否有权限 (有效关联记录数)
     */
    int checkNurseElderlyAccess(@Param("nurseId") Long nurseId, 
                                 @Param("elderlyId") Long elderlyId);
    
    /**
     * 统计护工当前照护的老人数量
     * 
     * @param nurseId 护工ID
     * @return 照护人数
     */
    int countCurrentElderlyByNurseId(@Param("nurseId") Long nurseId);
}
