package com.smartcare.cloud.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.smartcare.cloud.entity.DoctorElderlyRelation;

/**
 * 医生-老人关联Mapper单元测试
 * 
 * @author SmartCare Team
 * @date 2025-10-29
 */
@SpringBootTest
@Transactional
@DisplayName("医生-老人关联Mapper测试")
public class DoctorElderlyRelationMapperTest {

    @Autowired
    private DoctorElderlyRelationMapper doctorElderlyRelationMapper;

    private DoctorElderlyRelation testRelation;

    @BeforeEach
    void setUp() {
        testRelation = new DoctorElderlyRelation();
        testRelation.setDoctorId(1L);
        testRelation.setElderlyId(1L);
        testRelation.setRelationshipType("primary");
        testRelation.setStartDate(LocalDate.now());
        testRelation.setStatus(1);
        testRelation.setCreateTime(LocalDateTime.now());
        testRelation.setUpdateTime(LocalDateTime.now());
        testRelation.setIsDeleted(0);
    }

    @Test
    @DisplayName("插入医生-老人关联关系")
    void testInsert() {
        int result = doctorElderlyRelationMapper.insert(testRelation);
        assertEquals(1, result);
        assertNotNull(testRelation.getId());
    }

    @Test
    @DisplayName("查询医生负责的老人ID列表")
    void testSelectElderlyIdsByDoctorId() {
        // 先插入测试数据
        doctorElderlyRelationMapper.insert(testRelation);
        
        // 查询
        List<Long> elderlyIds = doctorElderlyRelationMapper.selectElderlyIdsByDoctorId(1L);
        assertNotNull(elderlyIds);
        assertTrue(elderlyIds.contains(1L));
    }

    @Test
    @DisplayName("检查医生访问老人权限")
    void testCheckDoctorElderlyAccess() {
        // 先插入测试数据
        doctorElderlyRelationMapper.insert(testRelation);
        
        // 检查有权限
        int hasAccess = doctorElderlyRelationMapper.checkDoctorElderlyAccess(1L, 1L);
        assertTrue(hasAccess > 0);
        
        // 检查无权限
        int noAccess = doctorElderlyRelationMapper.checkDoctorElderlyAccess(999L, 1L);
        assertEquals(0, noAccess);
    }

    @Test
    @DisplayName("查询主治医生ID")
    void testSelectPrimaryDoctorId() {
        // 先插入测试数据
        doctorElderlyRelationMapper.insert(testRelation);
        
        // 查询主治医生
        Long primaryDoctorId = doctorElderlyRelationMapper.selectPrimaryDoctorId(1L);
        assertNotNull(primaryDoctorId);
        assertEquals(1L, primaryDoctorId);
    }

    @Test
    @DisplayName("更新关联关系状态")
    void testUpdateStatus() {
        // 先插入
        doctorElderlyRelationMapper.insert(testRelation);
        
        // 更新状态
        testRelation.setStatus(0);
        int result = doctorElderlyRelationMapper.updateById(testRelation);
        assertEquals(1, result);
        
        // 验证状态已更新
        DoctorElderlyRelation updated = doctorElderlyRelationMapper.selectById(testRelation.getId());
        assertEquals(0, updated.getStatus());
    }

    @Test
    @DisplayName("删除关联关系(逻辑删除)")
    void testLogicalDelete() {
        // 先插入
        doctorElderlyRelationMapper.insert(testRelation);
        
        // 逻辑删除
        testRelation.setIsDeleted(1);
        doctorElderlyRelationMapper.updateById(testRelation);
        
        // 验证查询不到已删除的关联
        int hasAccess = doctorElderlyRelationMapper.checkDoctorElderlyAccess(1L, 1L);
        assertEquals(0, hasAccess);
    }
}
