package com.smartcare.cloud.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.smartcare.cloud.entity.Organization;

/**
 * 机构信息Mapper单元测试
 * 
 * @author SmartCare Team
 * @date 2025-10-29
 */
@SpringBootTest
@Transactional
@DisplayName("机构信息Mapper测试")
public class OrganizationMapperTest {

    @Autowired
    private OrganizationMapper organizationMapper;

    private Organization testOrg;

    @BeforeEach
    void setUp() {
        testOrg = new Organization();
        testOrg.setOrgCode("ORG001");
        testOrg.setOrgName("智慧医养示范中心");
        testOrg.setOrgType("nursing_home");
        testOrg.setOrgLevel(2);
        testOrg.setDirectorName("李院长");
        testOrg.setDirectorPhone("13800138000");
        testOrg.setContactPhone("010-12345678");
        testOrg.setContactEmail("contact@smartcare.com");
        testOrg.setAddress("北京市朝阳区XX路XX号");
        testOrg.setProvince("北京");
        testOrg.setCity("北京市");
        testOrg.setDistrict("朝阳区");
        testOrg.setTotalBeds(200);
        testOrg.setOccupiedBeds(150);
        testOrg.setBusinessLicense("91110000XXXXXXXXXX");
        testOrg.setServiceScope("养老服务、医疗护理、康复理疗");
        testOrg.setStatus(1);
        testOrg.setCreateTime(LocalDateTime.now());
        testOrg.setUpdateTime(LocalDateTime.now());
        testOrg.setIsDeleted(0);
    }

    @Test
    @DisplayName("插入机构信息")
    void testInsert() {
        int result = organizationMapper.insert(testOrg);
        assertEquals(1, result);
        assertNotNull(testOrg.getId());
    }

    @Test
    @DisplayName("根据ID查询机构")
    void testSelectById() {
        organizationMapper.insert(testOrg);
        
        Organization found = organizationMapper.selectById(testOrg.getId());
        assertNotNull(found);
        assertEquals("智慧医养示范中心", found.getOrgName());
        assertEquals("ORG001", found.getOrgCode());
    }

    @Test
    @DisplayName("更新机构信息")
    void testUpdate() {
        organizationMapper.insert(testOrg);
        
        testOrg.setOccupiedBeds(160);
        testOrg.setContactPhone("010-87654321");
        int result = organizationMapper.updateById(testOrg);
        assertEquals(1, result);
        
        Organization updated = organizationMapper.selectById(testOrg.getId());
        assertEquals(160, updated.getOccupiedBeds());
        assertEquals("010-87654321", updated.getContactPhone());
    }

    @Test
    @DisplayName("删除机构(逻辑删除)")
    void testLogicalDelete() {
        organizationMapper.insert(testOrg);
        
        testOrg.setIsDeleted(1);
        testOrg.setStatus(0);
        organizationMapper.updateById(testOrg);
        
        Organization found = organizationMapper.selectById(testOrg.getId());
        assertEquals(1, found.getIsDeleted());
        assertEquals(0, found.getStatus());
    }
}
