package com.smartcare.cloud.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.smartcare.cloud.dto.FamilyElderlyAccessDTO;
import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.entity.FamilyElderlyRelation;
import com.smartcare.cloud.entity.HealthRecord;
import com.smartcare.cloud.mapper.ElderlyMapper;
import com.smartcare.cloud.mapper.FamilyElderlyRelationMapper;
import com.smartcare.cloud.mapper.HealthRecordMapper;
import com.smartcare.cloud.mapper.HealthWarningMapper;
import com.smartcare.cloud.service.impl.FamilyPermissionServiceImpl;

/**
 * 家属权限服务单元测试
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@ExtendWith(MockitoExtension.class)
public class FamilyPermissionServiceTest {

    @Mock
    private FamilyElderlyRelationMapper familyElderlyRelationMapper;

    @Mock
    private ElderlyMapper elderlyMapper;

    @Mock
    private HealthRecordMapper healthRecordMapper;

    @Mock
    private HealthWarningMapper healthWarningMapper;

    @InjectMocks
    private FamilyPermissionServiceImpl familyPermissionService;

    private Elderly mockElderly;
    private FamilyElderlyRelation mockRelation;
    private HealthRecord mockHealthRecord;

    @BeforeEach
    void setUp() {
        // 模拟老人数据
        mockElderly = new Elderly();
        mockElderly.setId(1L);
        mockElderly.setName("张三");
        mockElderly.setAge(75);
        mockElderly.setGender(1); // 男
        mockElderly.setPhone("13800138000");
        mockElderly.setRoomNumber("101");
        mockElderly.setHealthStatus("良好");
        mockElderly.setCreateTime(LocalDateTime.now().minusYears(1));

        // 模拟家属关系数据
        mockRelation = new FamilyElderlyRelation();
        mockRelation.setId(1L);
        mockRelation.setFamilyId(1L);
        mockRelation.setElderlyId(1L);
        mockRelation.setRelationship("儿子");
        mockRelation.setAccessLevel("2"); // 中级权限
        mockRelation.setStatus(1); // 激活状态

        // 模拟健康记录数据
        mockHealthRecord = new HealthRecord();
        mockHealthRecord.setId(1L);
        mockHealthRecord.setElderlyId(1L);
        mockHealthRecord.setRecordDate(LocalDateTime.now());
        mockHealthRecord.setDiagnosis("健康状况良好");
        mockHealthRecord.setRecordType(1); // 体检记录
    }

    @Test
    void testCheckFamilyElderlyAccess_ValidPermission() {
        // 准备测试数据
        when(familyElderlyRelationMapper.selectByFamilyIdAndElderlyId(1L, 1L))
                .thenReturn(mockRelation);

        // 执行测试
        boolean hasPermission = familyPermissionService.checkFamilyElderlyAccess(1L, 1L);

        // 验证结果
        assertTrue(hasPermission);
        verify(familyElderlyRelationMapper, times(1))
                .selectByFamilyIdAndElderlyId(1L, 1L);
    }

    @Test
    void testCheckFamilyElderlyAccess_InvalidPermission() {
        // 准备测试数据
        when(familyElderlyRelationMapper.selectByFamilyIdAndElderlyId(1L, 99L))
                .thenReturn(null);

        // 执行测试
        boolean hasPermission = familyPermissionService.checkFamilyElderlyAccess(99L, 1L);

        // 验证结果
        assertFalse(hasPermission);
    }

    @Test
    void testGetFamilyElderlyList() {
        // 准备测试数据
        List<Long> elderlyIds = Arrays.asList(1L, 2L);
        List<Elderly> elderlyList = Arrays.asList(mockElderly);

        when(familyElderlyRelationMapper.selectElderlyIdsByFamilyId(1L))
                .thenReturn(elderlyIds);
        when(elderlyMapper.selectBatchIds(elderlyIds))
                .thenReturn(elderlyList);
        when(familyElderlyRelationMapper.selectByFamilyIdAndElderlyId(1L, 1L))
                .thenReturn(mockRelation);
        when(healthRecordMapper.selectLatestByElderlyId(1L))
                .thenReturn(mockHealthRecord);

        // 执行测试
        List<FamilyElderlyAccessDTO> result = familyPermissionService.getFamilyElderlyList(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());

        FamilyElderlyAccessDTO dto = result.get(0);
        assertEquals(1L, dto.getElderlyId());
        assertEquals("张三", dto.getName());
        assertEquals("男", dto.getGender());
        assertEquals("101", dto.getRoom());
        assertEquals("138****8000", dto.getPhone()); // 验证手机号脱敏
        assertEquals("儿子", dto.getRelationship());
        assertEquals("2", dto.getAccessLevel());
    }

    @Test
    void testGetHealthSummaryForFamily() {
        // 准备测试数据
        when(familyElderlyRelationMapper.selectByFamilyIdAndElderlyId(1L, 1L))
                .thenReturn(mockRelation);
        when(elderlyMapper.selectById(1L))
                .thenReturn(mockElderly);
        when(healthRecordMapper.selectLatestByElderlyId(1L))
                .thenReturn(mockHealthRecord);

        // 执行测试
        Map<String, Object> result = familyPermissionService.getHealthSummaryForFamily(1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("张三", result.get("elderlyName"));
        assertEquals("男", result.get("gender"));
        assertEquals("101", result.get("room"));
        assertTrue(result.containsKey("healthStatus"));
        assertTrue(result.containsKey("generalStatus"));
        assertTrue(result.containsKey("lastCheckupTime"));
        assertTrue(result.containsKey("alertLevel"));
        assertTrue(result.containsKey("healthTrend"));
    }

    @Test
    void testGetHealthSummaryForFamily_NoPermission() {
        // 准备测试数据
        when(familyElderlyRelationMapper.selectByFamilyIdAndElderlyId(1L, 99L))
                .thenReturn(null);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            familyPermissionService.getHealthSummaryForFamily(99L, 1L);
        });
    }

    @Test
    void testGetEmergencyAlertsForFamily() {
        // 准备测试数据
        when(familyElderlyRelationMapper.selectByFamilyIdAndElderlyId(1L, 1L))
                .thenReturn(mockRelation);
        when(healthWarningMapper.selectByElderlyIdAndTimeRange(eq(1L), any(), any()))
                .thenReturn(Arrays.asList());

        // 执行测试
        List<Map<String, Object>> result = familyPermissionService.getEmergencyAlertsForFamily(1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
