package com.smartcare.cloud.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.smartcare.cloud.entity.NurseInfo;

/**
 * 护工信息Mapper单元测试
 * 
 * @author SmartCare Team
 * @date 2025-10-29
 */
@SpringBootTest
@Transactional
@DisplayName("护工信息Mapper测试")
public class NurseInfoMapperTest {

    @Autowired
    private NurseInfoMapper nurseInfoMapper;

    private NurseInfo testNurse;

    @BeforeEach
    void setUp() {
        testNurse = new NurseInfo();
        testNurse.setUserId(100L);
        testNurse.setOrganizationId(1L);
        testNurse.setEmployeeNumber("N001");
        testNurse.setName("张护工");
        testNurse.setGender(0);
        testNurse.setPhone("13900000001");
        testNurse.setIdCard("110101199001011234");
        testNurse.setEntryDate(LocalDate.now());
        testNurse.setCertificateType("养老护理员证");
        testNurse.setCertificateNumber("CERT001");
        testNurse.setWorkArea("A区");
        testNurse.setMaxCareCount(5);
        testNurse.setStatus(1);
        testNurse.setCreateTime(LocalDateTime.now());
        testNurse.setUpdateTime(LocalDateTime.now());
        testNurse.setIsDeleted(0);
    }

    @Test
    @DisplayName("插入护工信息")
    void testInsert() {
        int result = nurseInfoMapper.insert(testNurse);
        assertEquals(1, result);
        assertNotNull(testNurse.getId());
    }

    @Test
    @DisplayName("根据ID查询护工")
    void testSelectById() {
        nurseInfoMapper.insert(testNurse);
        
        NurseInfo found = nurseInfoMapper.selectById(testNurse.getId());
        assertNotNull(found);
        assertEquals("张护工", found.getName());
        assertEquals("N001", found.getEmployeeNumber());
    }

    @Test
    @DisplayName("更新护工信息")
    void testUpdate() {
        nurseInfoMapper.insert(testNurse);
        
        testNurse.setPhone("13900000002");
        testNurse.setWorkArea("B区");
        int result = nurseInfoMapper.updateById(testNurse);
        assertEquals(1, result);
        
        NurseInfo updated = nurseInfoMapper.selectById(testNurse.getId());
        assertEquals("13900000002", updated.getPhone());
        assertEquals("B区", updated.getWorkArea());
    }

    @Test
    @DisplayName("删除护工(逻辑删除)")
    void testLogicalDelete() {
        nurseInfoMapper.insert(testNurse);
        
        testNurse.setIsDeleted(1);
        nurseInfoMapper.updateById(testNurse);
        
        NurseInfo found = nurseInfoMapper.selectById(testNurse.getId());
        assertEquals(1, found.getIsDeleted());
    }
}
