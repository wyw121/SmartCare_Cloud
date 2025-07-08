package com.smartcare.cloud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.service.ElderlyService;
import com.smartcare.cloud.vo.ResponseResult;

/**
 * 测试控制器 用于验证系统功能
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private ElderlyService elderlyService;

    @GetMapping("/elderly/all")
    public ResponseResult<List<Elderly>> getAllElderly() {
        try {
            List<Elderly> elderlyList = elderlyService.list();
            return ResponseResult.success(elderlyList);
        } catch (Exception e) {
            return ResponseResult.error("查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/elderly/batch-test")
    public ResponseResult<List<Elderly>> testBatchQuery() {
        try {
            List<Long> ids = List.of(1L, 2L);
            return elderlyService.getElderlyByIds(ids);
        } catch (Exception e) {
            return ResponseResult.error("批量查询测试失败：" + e.getMessage());
        }
    }
}
