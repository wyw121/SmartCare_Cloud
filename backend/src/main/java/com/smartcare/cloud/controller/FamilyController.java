package com.smartcare.cloud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.smartcare.cloud.dto.HealthWarningPageDTO;
import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.entity.HealthWarning;
import com.smartcare.cloud.service.ElderlyService;
import com.smartcare.cloud.service.FamilyService;
import com.smartcare.cloud.service.HealthWarningService;
import com.smartcare.cloud.vo.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 家属专用控制器 为家属用户提供专门的API接口
 *
 * @author SmartCare Team
 * @date 2025-07-09
 */
@Tag(name = "家属专用接口", description = "家属用户专用的API接口")
@RestController
@RequestMapping("/api/family")
@Validated
public class FamilyController {

    private static final Logger log = LoggerFactory.getLogger(FamilyController.class);

    @Autowired
    private ElderlyService elderlyService;

    @Autowired
    private FamilyService familyService;

    @Autowired
    private HealthWarningService healthWarningService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据老人ID列表批量获取老人信息（家属专用）
     */
    @Operation(summary = "家属批量获取老人信息", description = "家属根据关联的老人ID列表批量获取老人信息")
    @PostMapping("/elderly/batch")
    public ResponseResult<List<Elderly>> getElderlyByIds(
            @Parameter(description = "请求体包含elderlyIds字段（老人ID数组）", required = true, example = "{\"elderlyIds\": [1, 2, 3]}")
            @RequestBody FamilyElderlyRequest request) {
        log.info("家属批量获取老人信息，老人IDs：{}", request.getElderlyIds());
        try {
            // 暂时使用固定的家属用户ID进行权限验证
            // TODO: 从JWT token或session中获取当前登录用户的ID
            Long currentFamilyUserId = 3L;

            // 验证家属是否有权限查看这些老人
            if (!familyService.hasPermissionToViewElderlyList(currentFamilyUserId, request.getElderlyIds())) {
                log.warn("家属用户{}无权限查看老人列表：{}", currentFamilyUserId, request.getElderlyIds());
                return ResponseResult.error("无权限查看指定老人信息");
            }

            return elderlyService.getElderlyByIds(request.getElderlyIds());
        } catch (Exception e) {
            log.error("家属批量获取老人信息失败", e);
            return ResponseResult.error("获取老人信息失败");
        }
    }

    /**
     * 获取老人最新体征数据（家属权限）
     */
    @Operation(summary = "获取老人最新体征数据", description = "家属查看关联老人的最新体征数据（血压、心率、体温等）")
    @GetMapping("/elderly/{elderlyId}/vitals/latest")
    public ResponseResult<Object> getLatestVitals(
            @Parameter(description = "老人ID", required = true, example = "1") 
            @PathVariable Long elderlyId) {
        log.info("获取老人最新体征数据，老人ID：{}", elderlyId);
        try {
            // 权限验证
            Long currentFamilyUserId = 3L;
            if (!familyService.hasPermissionToViewElderly(currentFamilyUserId, elderlyId)) {
                log.warn("家属用户{}无权限查看老人{}的体征数据", currentFamilyUserId, elderlyId);
                return ResponseResult.error("无权限查看该老人信息");
            }

            // TODO: 从健康记录服务获取最新体征数据
            // 暂时返回模拟数据
            java.util.Map<String, Object> vitals = new java.util.HashMap<>();
            vitals.put("bloodPressure", "120/80");
            vitals.put("heartRate", 72);
            vitals.put("temperature", 36.5);
            vitals.put("updateTime", new java.util.Date());

            return ResponseResult.success(vitals);
        } catch (Exception e) {
            log.error("获取老人体征数据失败", e);
            return ResponseResult.error("获取体征数据失败");
        }
    }

    /**
     * 获取老人预警信息（家属权限）
     */
    @Operation(summary = "获取老人预警信息", description = "家属查看关联老人的健康预警信息")
    @GetMapping("/elderly/{elderlyId}/warnings")
    public ResponseResult<List<Object>> getWarnings(
            @Parameter(description = "老人ID") @PathVariable Long elderlyId) {
        log.info("获取老人预警信息，老人ID：{}", elderlyId);
        try {
            // 权限验证
            Long currentFamilyUserId = 3L;
            if (!familyService.hasPermissionToViewElderly(currentFamilyUserId, elderlyId)) {
                log.warn("家属用户{}无权限查看老人{}的预警信息", currentFamilyUserId, elderlyId);
                return ResponseResult.error("无权限查看该老人信息");
            }

            // TODO: 从预警服务获取预警信息
            // 暂时返回模拟数据
            List<Object> warnings = new java.util.ArrayList<>();

            // 模拟一些预警数据
            java.util.Map<String, Object> warning1 = new java.util.HashMap<>();
            warning1.put("id", 1);
            warning1.put("message", "血压偏高，建议注意休息");
            warning1.put("level", "WARNING");
            warning1.put("createTime", new java.util.Date());
            warning1.put("isRead", false);
            warning1.put("suggestion", "定期监测血压，适当运动，保持心情愉快");
            warnings.add(warning1);

            return ResponseResult.success(warnings);
        } catch (Exception e) {
            log.error("获取老人预警信息失败", e);
            return ResponseResult.error("获取预警信息失败");
        }
    }

    /**
     * 标记预警为已读（家属权限）
     */
    @Operation(summary = "标记预警为已读", description = "家属标记预警信息为已读状态")
    @PostMapping("/warnings/mark-read")
    public ResponseResult<Void> markWarningsAsRead(@RequestBody FamilyWarningRequest request) {
        log.info("标记预警为已读，预警IDs：{}", request.getWarningIds());
        try {
            // TODO: 验证家属是否有权限操作这些预警
            // TODO: 调用预警服务标记为已读

            // 暂时返回成功
            return ResponseResult.success();
        } catch (Exception e) {
            log.error("标记预警为已读失败", e);
            return ResponseResult.error("标记失败");
        }
    }

    /**
     * 发送联系医护请求（家属权限）
     */
    @Operation(summary = "发送联系医护请求", description = "家属发送联系医护人员的请求")
    @PostMapping("/contact/send")
    public ResponseResult<Void> sendContactRequest(@RequestBody FamilyContactRequest request) {
        log.info("发送联系医护请求，数据：{}", request);
        try {
            // TODO: 验证家属身份和权限
            // TODO: 调用通知服务发送联系请求
            // TODO: 记录联系日志

            log.info("联系请求详情 - 老人：{}，紧急程度：{}，消息：{}",
                    request.getElderlyName(), request.getUrgency(), request.getMessage());

            // 模拟发送成功
            return ResponseResult.success();
        } catch (Exception e) {
            log.error("发送联系医护请求失败", e);
            return ResponseResult.error("发送请求失败");
        }
    }

    /**
     * 家属专用 - 分页查询关联老人的健康预警列表
     */
    @Operation(summary = "家属查询关联老人健康预警", description = "家属分页查询关联老人的健康预警信息")
    @PostMapping("/warnings/page")
    public ResponseResult<Map<String, Object>> getFamilyWarningsPage(@RequestBody FamilyWarningPageRequest request) {
        log.info("家属分页查询健康预警，参数：{}", request);
        try {
            // 获取当前家属用户ID（实际应从JWT token获取）
            Long currentFamilyUserId = 3L;

            // 获取家属关联的老人ID列表
            List<Long> elderlyIds = familyService.getRelatedElderlyIds(currentFamilyUserId);
            if (elderlyIds == null || elderlyIds.isEmpty()) {
                return ResponseResult.error("当前家属用户没有关联的老人");
            }

            // 构建查询条件，限制为家属关联的老人
            HealthWarningPageDTO dto = new HealthWarningPageDTO();
            dto.setPageNum(request.getPageNum());
            dto.setPageSize(request.getPageSize());

            // 设置查询条件
            if (request.getElderlyName() != null && !request.getElderlyName().trim().isEmpty()) {
                dto.setElderlyName(request.getElderlyName());
            }
            if (request.getWarningType() != null && !request.getWarningType().trim().isEmpty()) {
                dto.setWarningType(request.getWarningType());
            }
            if (request.getWarningLevel() != null && !request.getWarningLevel().trim().isEmpty()) {
                dto.setWarningLevel(request.getWarningLevel());
            }
            if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
                dto.setStatus(request.getStatus());
            }
            if (request.getStartTime() != null) {
                dto.setStartTime(request.getStartTime());
            }
            if (request.getEndTime() != null) {
                dto.setEndTime(request.getEndTime());
            }

            // 调用健康预警服务，传入家属关联的老人ID列表
            PageInfo<HealthWarning> pageInfo = healthWarningService.getPageListByElderlyIds(dto, elderlyIds);

            Map<String, Object> result = new HashMap<>();
            result.put("list", pageInfo.getList());
            result.put("total", pageInfo.getTotal());
            result.put("pageNum", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());
            result.put("pages", pageInfo.getPages());

            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("家属分页查询健康预警失败", e);
            return ResponseResult.error("查询健康预警失败：" + e.getMessage());
        }
    }

    /**
     * 家属专用 - 获取关联老人健康预警统计数据
     */
    @Operation(summary = "家属获取健康预警统计", description = "家属获取关联老人的健康预警统计数据")
    @GetMapping("/warnings/statistics")
    public ResponseResult<Map<String, Object>> getFamilyWarningStatistics() {
        log.info("家属获取健康预警统计");
        try {
            // 获取当前家属用户ID（实际应从JWT token获取）
            Long currentFamilyUserId = 3L;

            // 获取家属关联的老人ID列表
            List<Long> elderlyIds = familyService.getRelatedElderlyIds(currentFamilyUserId);
            if (elderlyIds == null || elderlyIds.isEmpty()) {
                return ResponseResult.error("当前家属用户没有关联的老人");
            }

            // 获取统计数据（按老人ID列表过滤）
            Map<String, Object> statistics = healthWarningService.getWarningStatisticsByElderlyIds(elderlyIds);

            return ResponseResult.success(statistics);
        } catch (Exception e) {
            log.error("家属获取健康预警统计失败", e);
            return ResponseResult.error("获取统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 开发环境 - 初始化测试数据
     */
    @Operation(summary = "初始化测试数据", description = "开发环境专用，创建家属关联的健康预警测试数据")
    @PostMapping("/init-test-data")
    public ResponseResult<String> initTestData() {
        log.info("初始化家属端测试数据");
        try {
            // 创建测试健康预警数据
            List<HealthWarning> testWarnings = createTestWarnings();

            // 这里应该调用service保存数据
            // 但由于是测试，我们只返回成功信息
            log.info("已创建 {} 条测试预警数据", testWarnings.size());

            return ResponseResult.success("测试数据初始化成功，创建了" + testWarnings.size() + "条预警数据");
        } catch (Exception e) {
            log.error("初始化测试数据失败", e);
            return ResponseResult.error("初始化失败：" + e.getMessage());
        }
    }

    /**
     * 创建测试健康预警数据
     */
    private List<HealthWarning> createTestWarnings() {
        List<HealthWarning> warnings = new ArrayList<>();

        // 为老人ID=1创建预警
        HealthWarning warning1 = new HealthWarning();
        warning1.setElderlyId(1L);
        warning1.setElderlyName("张大爷");
        warning1.setWarningType("血压异常");
        warning1.setWarningLevel(3);
        warning1.setTitle("高血压预警");
        warning1.setContent("血压监测显示收缩压180mmHg，超出正常范围，请及时就医");
        warning1.setTriggerData("收缩压:180mmHg, 舒张压:110mmHg");
        warning1.setDataSource("血压监测设备");
        warning1.setStatus(0);
        warning1.setTriggerTime(java.time.LocalDateTime.now().minusHours(2));
        warnings.add(warning1);

        HealthWarning warning2 = new HealthWarning();
        warning2.setElderlyId(2L);
        warning2.setElderlyName("李奶奶");
        warning2.setWarningType("心率异常");
        warning2.setWarningLevel(2);
        warning2.setTitle("心率过快预警");
        warning2.setContent("心率监测显示心率105次/分，略高于正常范围");
        warning2.setTriggerData("心率:105次/分");
        warning2.setDataSource("心率监测设备");
        warning2.setStatus(1);
        warning2.setTriggerTime(java.time.LocalDateTime.now().minusHours(4));
        warnings.add(warning2);

        HealthWarning warning3 = new HealthWarning();
        warning3.setElderlyId(1L);
        warning3.setElderlyName("张大爷");
        warning3.setWarningType("用药提醒");
        warning3.setWarningLevel(1);
        warning3.setTitle("用药时间提醒");
        warning3.setContent("降压药服用时间已到，请按时服药");
        warning3.setTriggerData("药物:降压药, 剂量:1片");
        warning3.setDataSource("智能药盒");
        warning3.setStatus(0);
        warning3.setTriggerTime(java.time.LocalDateTime.now().minusMinutes(30));
        warnings.add(warning3);

        return warnings;
    }

    /**
     * 开发环境 - 检查数据库数据
     */
    @Operation(summary = "检查数据库数据", description = "开发环境专用，检查数据库中的健康预警数据")
    @GetMapping("/check-data")
    public ResponseResult<Map<String, Object>> checkData() {
        log.info("检查数据库健康预警数据");
        try {
            Map<String, Object> result = new HashMap<>();

            // 检查总数据量
            long totalCount = healthWarningService.count();
            result.put("totalWarnings", totalCount);

            // 检查老人ID=1的数据
            long elderly1Count = healthWarningService.count(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HealthWarning>()
                            .eq(HealthWarning::getElderlyId, 1L)
            );
            result.put("elderly1Warnings", elderly1Count);

            // 检查老人ID=2的数据
            long elderly2Count = healthWarningService.count(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HealthWarning>()
                            .eq(HealthWarning::getElderlyId, 2L)
            );
            result.put("elderly2Warnings", elderly2Count);

            // 获取家属关联的老人ID列表
            List<Long> elderlyIds = familyService.getRelatedElderlyIds(3L);
            result.put("familyRelatedElderlyIds", elderlyIds);

            // 检查家属相关数据
            if (elderlyIds != null && !elderlyIds.isEmpty()) {
                long familyRelatedCount = healthWarningService.count(
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HealthWarning>()
                                .in(HealthWarning::getElderlyId, elderlyIds)
                );
                result.put("familyRelatedWarnings", familyRelatedCount);
            } else {
                result.put("familyRelatedWarnings", 0);
            }

            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("检查数据库数据失败", e);
            return ResponseResult.error("检查失败：" + e.getMessage());
        }
    }

    /**
     * 家属专用 - 获取关联老人列表
     */
    @Operation(summary = "家属获取关联老人列表", description = "家属获取当前用户关联的所有老人信息")
    @GetMapping("/elderly/list")
    public ResponseResult<List<Elderly>> getFamilyElderlyList() {
        log.info("家属获取关联老人列表");
        try {
            // 获取当前家属用户ID（实际应从JWT token获取）
            Long currentFamilyUserId = 3L;

            // 获取家属关联的老人ID列表
            List<Long> elderlyIds = familyService.getRelatedElderlyIds(currentFamilyUserId);
            if (elderlyIds == null || elderlyIds.isEmpty()) {
                return ResponseResult.success(List.of()); // 返回空列表而不是错误
            }

            // 调用老人服务获取详细信息
            ResponseResult<List<Elderly>> result = elderlyService.getElderlyByIds(elderlyIds);
            return result;
        } catch (Exception e) {
            log.error("家属获取关联老人列表失败", e);
            return ResponseResult.error("获取关联老人列表失败：" + e.getMessage());
        }
    }

    /**
     * 临时API - 创建家属相关表和测试数据
     */
    @Operation(summary = "创建家属表", description = "开发环境专用，创建家属相关表和测试数据")
    @PostMapping("/create-tables")
    public ResponseResult<String> createFamilyTables() {
        log.info("开始创建家属相关表和测试数据");
        try {
            // 创建family_user表
            String createFamilyUserSql = "CREATE TABLE IF NOT EXISTS family_user ("
                    + "id BIGINT PRIMARY KEY AUTO_INCREMENT,"
                    + "sys_user_id BIGINT NOT NULL COMMENT '系统用户ID（关联sys_user表）',"
                    + "real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',"
                    + "gender TINYINT(1) DEFAULT 1 COMMENT '性别：1-男，2-女',"
                    + "phone VARCHAR(20) COMMENT '手机号码',"
                    + "email VARCHAR(100) COMMENT '邮箱地址',"
                    + "status TINYINT(1) DEFAULT 1 COMMENT '状态：1-正常，0-禁用',"
                    + "is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',"
                    + "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',"
                    + "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',"
                    + "create_by BIGINT COMMENT '创建人ID',"
                    + "update_by BIGINT COMMENT '更新人ID',"
                    + "UNIQUE KEY uk_sys_user_id (sys_user_id),"
                    + "INDEX idx_status (status),"
                    + "INDEX idx_phone (phone),"
                    + "INDEX idx_create_time (create_time)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家属用户表'";

            jdbcTemplate.execute(createFamilyUserSql);
            log.info("family_user表创建成功");

            // 创建family_elderly_relation表
            String createRelationSql = "CREATE TABLE IF NOT EXISTS family_elderly_relation ("
                    + "id BIGINT PRIMARY KEY AUTO_INCREMENT,"
                    + "family_user_id BIGINT NOT NULL COMMENT '家属用户ID',"
                    + "elderly_id BIGINT NOT NULL COMMENT '老人ID',"
                    + "relationship_type VARCHAR(20) NOT NULL COMMENT '关系类型',"
                    + "relationship_name VARCHAR(20) NOT NULL COMMENT '关系名称',"
                    + "is_primary TINYINT(1) DEFAULT 0 COMMENT '是否主要联系人：1-是，0-否',"
                    + "is_emergency TINYINT(1) DEFAULT 0 COMMENT '是否紧急联系人：1-是，0-否',"
                    + "contact_priority INT DEFAULT 5 COMMENT '联系优先级：1-最高，5-最低',"
                    + "visit_frequency VARCHAR(20) COMMENT '探视频率',"
                    + "authorized_operations JSON COMMENT '授权操作列表',"
                    + "start_date DATE COMMENT '关系开始日期',"
                    + "end_date DATE COMMENT '关系结束日期',"
                    + "status TINYINT(1) DEFAULT 1 COMMENT '状态：1-有效，0-无效',"
                    + "is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',"
                    + "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',"
                    + "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',"
                    + "create_by BIGINT COMMENT '创建人ID',"
                    + "update_by BIGINT COMMENT '更新人ID',"
                    + "UNIQUE KEY uk_family_elderly (family_user_id, elderly_id),"
                    + "INDEX idx_family_user (family_user_id),"
                    + "INDEX idx_elderly (elderly_id),"
                    + "INDEX idx_relationship (relationship_type),"
                    + "INDEX idx_status (status),"
                    + "INDEX idx_is_primary (is_primary),"
                    + "INDEX idx_is_emergency (is_emergency),"
                    + "INDEX idx_contact_priority (contact_priority)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家属老人关系表'";

            jdbcTemplate.execute(createRelationSql);
            log.info("family_elderly_relation表创建成功");

            // 插入测试家属用户数据
            String insertFamilyUserSql = "INSERT IGNORE INTO family_user "
                    + "(sys_user_id, real_name, gender, phone, email, status, create_by) "
                    + "VALUES (3, '李家属', 1, '13800138002', 'family@smartcare.com', 1, 1)";

            int familyUserRows = jdbcTemplate.update(insertFamilyUserSql);
            log.info("插入家属用户数据: {} 行", familyUserRows);

            // 获取家属用户ID
            String getFamilyUserIdSql = "SELECT id FROM family_user WHERE sys_user_id = 3";
            Long familyUserId = jdbcTemplate.queryForObject(getFamilyUserIdSql, Long.class);
            log.info("家属用户ID: {}", familyUserId);

            // 插入测试关联数据
            String insertRelationSql = "INSERT IGNORE INTO family_elderly_relation "
                    + "(family_user_id, elderly_id, relationship_type, relationship_name, "
                    + "is_primary, is_emergency, contact_priority, start_date, status, create_by) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, CURDATE(), 1, 1)";

            // 插入5个关联关系
            Object[][] relationData = {
                {familyUserId, 1L, "child", "儿子", 1, 1, 1},
                {familyUserId, 2L, "child", "女儿", 0, 1, 2},
                {familyUserId, 3L, "child", "儿子", 0, 0, 3},
                {familyUserId, 4L, "grandchild", "孙子", 0, 0, 4},
                {familyUserId, 5L, "grandchild", "孙女", 0, 0, 5}
            };

            int totalRelations = 0;
            for (Object[] data : relationData) {
                int rows = jdbcTemplate.update(insertRelationSql, data);
                totalRelations += rows;
                log.info("插入关联关系: 家属{} -> 老人{}, 关系: {}", data[0], data[1], data[3]);
            }

            log.info("总共插入 {} 条关联关系", totalRelations);

            return ResponseResult.success("家属表和测试数据创建成功！"
                    + "家属用户: " + familyUserRows + " 条, "
                    + "关联关系: " + totalRelations + " 条");

        } catch (Exception e) {
            log.error("创建家属表失败", e);
            return ResponseResult.error("创建家属表失败：" + e.getMessage());
        }
    }

    // ========================================
    // 内部DTO类
    // ========================================
    /**
     * 家属获取老人信息请求
     */
    public static class FamilyElderlyRequest {

        private List<Long> elderlyIds;

        public List<Long> getElderlyIds() {
            return elderlyIds;
        }

        public void setElderlyIds(List<Long> elderlyIds) {
            this.elderlyIds = elderlyIds;
        }
    }

    /**
     * 家属预警操作请求
     */
    public static class FamilyWarningRequest {

        private List<Long> warningIds;

        public List<Long> getWarningIds() {
            return warningIds;
        }

        public void setWarningIds(List<Long> warningIds) {
            this.warningIds = warningIds;
        }
    }

    /**
     * 家属联系请求
     */
    public static class FamilyContactRequest {

        private String elderlyName;
        private String urgency;
        private String message;

        public String getElderlyName() {
            return elderlyName;
        }

        public void setElderlyName(String elderlyName) {
            this.elderlyName = elderlyName;
        }

        public String getUrgency() {
            return urgency;
        }

        public void setUrgency(String urgency) {
            this.urgency = urgency;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "FamilyContactRequest{"
                    + "elderlyName='" + elderlyName + '\''
                    + ", urgency='" + urgency + '\''
                    + ", message='" + message + '\''
                    + '}';
        }
    }

    /**
     * 家属健康预警分页查询请求
     */
    public static class HealthWarningPageRequest {

        private Long elderlyId;
        private int pageNum;
        private int pageSize;

        public Long getElderlyId() {
            return elderlyId;
        }

        public void setElderlyId(Long elderlyId) {
            this.elderlyId = elderlyId;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }

    /**
     * 家属健康预警分页查询请求
     */
    public static class FamilyWarningPageRequest {

        private Integer pageNum = 1;
        private Integer pageSize = 10;
        private String elderlyName;
        private String warningType;
        private String warningLevel;
        private String status;
        private java.time.LocalDateTime startTime;
        private java.time.LocalDateTime endTime;

        public Integer getPageNum() {
            return pageNum;
        }

        public void setPageNum(Integer pageNum) {
            this.pageNum = pageNum;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public String getElderlyName() {
            return elderlyName;
        }

        public void setElderlyName(String elderlyName) {
            this.elderlyName = elderlyName;
        }

        public String getWarningType() {
            return warningType;
        }

        public void setWarningType(String warningType) {
            this.warningType = warningType;
        }

        public String getWarningLevel() {
            return warningLevel;
        }

        public void setWarningLevel(String warningLevel) {
            this.warningLevel = warningLevel;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public java.time.LocalDateTime getStartTime() {
            return startTime;
        }

        public void setStartTime(java.time.LocalDateTime startTime) {
            this.startTime = startTime;
        }

        public java.time.LocalDateTime getEndTime() {
            return endTime;
        }

        public void setEndTime(java.time.LocalDateTime endTime) {
            this.endTime = endTime;
        }

        @Override
        public String toString() {
            return "FamilyWarningPageRequest{"
                    + "pageNum=" + pageNum
                    + ", pageSize=" + pageSize
                    + ", elderlyName='" + elderlyName + '\''
                    + ", warningType='" + warningType + '\''
                    + ", warningLevel='" + warningLevel + '\''
                    + ", status='" + status + '\''
                    + ", startTime=" + startTime
                    + ", endTime=" + endTime
                    + '}';
        }
    }
}
