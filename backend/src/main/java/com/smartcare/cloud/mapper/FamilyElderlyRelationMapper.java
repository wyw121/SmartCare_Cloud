package com.smartcare.cloud.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartcare.cloud.entity.FamilyElderlyRelation;

/**
 * 家属老人关联关系Mapper接口
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Mapper
public interface FamilyElderlyRelationMapper extends BaseMapper<FamilyElderlyRelation> {

    /**
     * 根据家属ID和老人ID查询关联关系
     *
     * @param familyId 家属ID
     * @param elderlyId 老人ID
     * @return 关联关系
     */
    @Select("SELECT * FROM family_elderly_relation "
            + "WHERE family_id = #{familyId} AND elderly_id = #{elderlyId} "
            + "AND is_deleted = 0")
    FamilyElderlyRelation selectByFamilyIdAndElderlyId(@Param("familyId") Long familyId,
            @Param("elderlyId") Long elderlyId);

    /**
     * 根据家属ID查询关联的老人ID列表
     *
     * @param familyId 家属ID
     * @return 老人ID列表
     */
    @Select("SELECT elderly_id FROM family_elderly_relation "
            + "WHERE family_id = #{familyId} AND status = 1 AND is_deleted = 0")
    List<Long> selectElderlyIdsByFamilyId(@Param("familyId") Long familyId);

    /**
     * 根据老人ID查询关联的家属ID列表
     *
     * @param elderlyId 老人ID
     * @return 家属ID列表
     */
    @Select("SELECT family_id FROM family_elderly_relation "
            + "WHERE elderly_id = #{elderlyId} AND status = 1 AND is_deleted = 0")
    List<Long> selectFamilyIdsByElderlyId(@Param("elderlyId") Long elderlyId);

    /**
     * 查询家属可查看的老人关联信息列表
     *
     * @param familyId 家属ID
     * @return 关联关系列表
     */
    @Select("SELECT fer.*, e.name as elderly_name, e.age, e.gender "
            + "FROM family_elderly_relation fer "
            + "LEFT JOIN elderly e ON fer.elderly_id = e.id "
            + "WHERE fer.family_id = #{familyId} AND fer.status = 1 "
            + "AND fer.is_deleted = 0 AND e.is_deleted = 0")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "familyId", column = "family_id"),
        @Result(property = "elderlyId", column = "elderly_id"),
        @Result(property = "relationship", column = "relationship"),
        @Result(property = "accessLevel", column = "access_level"),
        @Result(property = "status", column = "status")
    })
    List<FamilyElderlyRelation> selectFamilyElderlyRelations(@Param("familyId") Long familyId);

    /**
     * 检查家属是否有访问指定老人的权限
     *
     * @param familyId 家属ID
     * @param elderlyId 老人ID
     * @return 是否有权限（返回记录数）
     */
    @Select("SELECT COUNT(*) FROM family_elderly_relation "
            + "WHERE family_id = #{familyId} AND elderly_id = #{elderlyId} "
            + "AND status = 1 AND is_deleted = 0 "
            + "AND (start_date IS NULL OR start_date <= NOW()) "
            + "AND (end_date IS NULL OR end_date >= NOW())")
    int checkFamilyAccess(@Param("familyId") Long familyId, @Param("elderlyId") Long elderlyId);

    /**
     * 获取老人的主要家属联系人
     *
     * @param elderlyId 老人ID
     * @return 主要家属联系人
     */
    @Select("SELECT * FROM family_elderly_relation "
            + "WHERE elderly_id = #{elderlyId} AND is_primary_contact = 1 "
            + "AND status = 1 AND is_deleted = 0 "
            + "ORDER BY create_time ASC LIMIT 1")
    FamilyElderlyRelation selectPrimaryFamilyContact(@Param("elderlyId") Long elderlyId);

    /**
     * 批量插入家属老人关联关系
     *
     * @param relations 关联关系列表
     */
    @Insert("<script>"
            + "INSERT INTO family_elderly_relation "
            + "(family_id, elderly_id, relationship, access_level, status, "
            + "start_date, end_date, is_primary_contact, remark, create_time, create_by) "
            + "VALUES "
            + "<foreach collection='relations' item='relation' separator=','>"
            + "(#{relation.familyId}, #{relation.elderlyId}, #{relation.relationship}, "
            + "#{relation.accessLevel}, #{relation.status}, #{relation.startDate}, "
            + "#{relation.endDate}, #{relation.isPrimaryContact}, #{relation.remark}, "
            + "NOW(), #{relation.createBy})"
            + "</foreach>"
            + "</script>")
    void batchInsert(@Param("relations") List<FamilyElderlyRelation> relations);

    /**
     * 更新关联关系状态
     *
     * @param id 关联关系ID
     * @param status 新状态
     * @param updateBy 更新者ID
     */
    @Update("UPDATE family_elderly_relation "
            + "SET status = #{status}, update_time = NOW(), update_by = #{updateBy} "
            + "WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updateBy") Long updateBy);

    /**
     * 删除家属老人关联关系（逻辑删除）
     *
     * @param familyId 家属ID
     * @param elderlyId 老人ID
     * @param updateBy 更新者ID
     */
    @Update("UPDATE family_elderly_relation "
            + "SET is_deleted = 1, update_time = NOW(), update_by = #{updateBy} "
            + "WHERE family_id = #{familyId} AND elderly_id = #{elderlyId}")
    void deleteFamilyElderlyRelation(@Param("familyId") Long familyId,
            @Param("elderlyId") Long elderlyId,
            @Param("updateBy") Long updateBy);
}
