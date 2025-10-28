package com.smartcare.cloud.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.mapper.ElderlyMapper;
import com.smartcare.cloud.service.impl.ElderlyServiceImpl;

/**
 * 老人服务单元测试
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("老人服务单元测试")
public class ElderlyServiceTest {

    @Mock
    private ElderlyMapper elderlyMapper;

    @InjectMocks
    private ElderlyServiceImpl elderlyService;

    private Elderly testElderly;

    @BeforeEach
    void setUp() {
        testElderly = new Elderly();
        testElderly.setId(1L);
        testElderly.setName("张三");
        testElderly.setGender(1);
        testElderly.setAge(75);
        testElderly.setIdCard("110101195001011234");
        testElderly.setPhone("13800138000");
        testElderly.setHealthStatus("HEALTHY");
        testElderly.setCareLevel(1);
        testElderly.setCreatedTime(LocalDateTime.now());
    }

    @Test
    @DisplayName("根据ID查询老人 - 成功")
    void testGetById_Success() {
        // Given
        when(elderlyMapper.selectById(1L)).thenReturn(testElderly);

        // When
        Elderly result = elderlyService.getById(1L);

        // Then
        assertNotNull(result);
        assertEquals("张三", result.getName());
        assertEquals(75, result.getAge());
        assertEquals("HEALTHY", result.getHealthStatus());
        verify(elderlyMapper, times(1)).selectById(1L);
    }

    @Test
    @DisplayName("根据ID查询老人 - 不存在")
    void testGetById_NotFound() {
        // Given
        when(elderlyMapper.selectById(999L)).thenReturn(null);

        // When
        Elderly result = elderlyService.getById(999L);

        // Then
        assertNull(result);
        verify(elderlyMapper, times(1)).selectById(999L);
    }

    @Test
    @DisplayName("保存老人信息 - 成功")
    void testSave_Success() {
        // Given
        when(elderlyMapper.insert(any(Elderly.class))).thenReturn(1);

        // When
        boolean result = elderlyService.save(testElderly);

        // Then
        assertTrue(result);
        verify(elderlyMapper, times(1)).insert(testElderly);
    }

    @Test
    @DisplayName("保存老人信息 - 失败")
    void testSave_Failure() {
        // Given
        when(elderlyMapper.insert(any(Elderly.class))).thenReturn(0);

        // When
        boolean result = elderlyService.save(testElderly);

        // Then
        assertFalse(result);
        verify(elderlyMapper, times(1)).insert(testElderly);
    }

    @Test
    @DisplayName("更新老人信息 - 成功")
    void testUpdate_Success() {
        // Given
        testElderly.setName("李四");
        when(elderlyMapper.updateById(any(Elderly.class))).thenReturn(1);

        // When
        boolean result = elderlyService.updateById(testElderly);

        // Then
        assertTrue(result);
        assertEquals("李四", testElderly.getName());
        verify(elderlyMapper, times(1)).updateById(testElderly);
    }

    @Test
    @DisplayName("更新老人信息 - 记录不存在")
    void testUpdate_NotFound() {
        // Given
        when(elderlyMapper.updateById(any(Elderly.class))).thenReturn(0);

        // When
        boolean result = elderlyService.updateById(testElderly);

        // Then
        assertFalse(result);
        verify(elderlyMapper, times(1)).updateById(testElderly);
    }

    @Test
    @DisplayName("删除老人信息 - 成功")
    void testDelete_Success() {
        // Given
        when(elderlyMapper.deleteById(1L)).thenReturn(1);

        // When
        boolean result = elderlyService.removeById(1L);

        // Then
        assertTrue(result);
        verify(elderlyMapper, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("删除老人信息 - 记录不存在")
    void testDelete_NotFound() {
        // Given
        when(elderlyMapper.deleteById(999L)).thenReturn(0);

        // When
        boolean result = elderlyService.removeById(999L);

        // Then
        assertFalse(result);
        verify(elderlyMapper, times(1)).deleteById(999L);
    }

    @Test
    @DisplayName("批量删除老人 - 成功")
    void testBatchDelete_Success() {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(elderlyMapper.deleteBatchIds(ids)).thenReturn(3);

        // When
        boolean result = elderlyService.removeByIds(ids);

        // Then
        assertTrue(result);
        verify(elderlyMapper, times(1)).deleteBatchIds(ids);
    }

    @Test
    @DisplayName("批量删除老人 - 空列表")
    void testBatchDelete_EmptyList() {
        // Given
        List<Long> ids = Arrays.asList();

        // When
        boolean result = elderlyService.removeByIds(ids);

        // Then
        assertFalse(result);
        verify(elderlyMapper, never()).deleteBatchIds(anyList());
    }

    @Test
    @DisplayName("查询所有老人 - 成功")
    void testListAll_Success() {
        // Given
        List<Elderly> elderlyList = Arrays.asList(testElderly);
        when(elderlyMapper.selectList(any())).thenReturn(elderlyList);

        // When
        List<Elderly> result = elderlyService.list();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("张三", result.get(0).getName());
        verify(elderlyMapper, times(1)).selectList(any());
    }

    @Test
    @DisplayName("查询所有老人 - 空列表")
    void testListAll_EmptyList() {
        // Given
        when(elderlyMapper.selectList(any())).thenReturn(Arrays.asList());

        // When
        List<Elderly> result = elderlyService.list();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(elderlyMapper, times(1)).selectList(any());
    }
}
