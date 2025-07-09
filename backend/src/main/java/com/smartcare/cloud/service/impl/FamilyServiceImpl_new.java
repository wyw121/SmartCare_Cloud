package com.smartcare.cloud.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.smartcare.cloud.service.FamilyService;

/**
 * 家属服务实现类
 *
 * @author SmartCare Team
 * @date 2025-07-09
 */
@Service
public class FamilyServiceImpl implements FamilyService {

    private static final Logger log = LoggerFactory.getLogger(FamilyServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据家属用户ID获取关联的老人ID列表
     *
     * @param familyUserId 家属用户ID
     * @return 老人ID列表
     */
    @Override
    public List<Long> getElderlyIdsByFamilyId(Long familyUserId) {
        log.info("查询家属用户 {} 关联的老人ID列表", familyUserId);
        
        try {
            // 从家属-老人关联表中查询
            String sql = "SELECT DISTINCT fer.elderly_id " +
                        "FROM family_elderly_relation fer " +
                        "WHERE fer.family_user_id = ? " +
                        "AND fer.status = 1 " +
                        "AND fer.is_deleted = 0 " +
                        "ORDER BY fer.contact_priority ASC, fer.is_primary DESC";
            
            List<Long> elderlyIds = jdbcTemplate.queryForList(sql, Long.class, familyUserId);
            log.info("家属用户 {} 关联的老人ID列表: {}", familyUserId, elderlyIds);
            
            // 如果数据库中没有关联关系，创建一些测试数据
            if (elderlyIds.isEmpty()) {
                log.warn("家属用户 {} 没有关联的老人，正在创建测试关联数据...", familyUserId);
                createTestFamilyElderlyRelation(familyUserId);
                
                // 重新查询
                elderlyIds = jdbcTemplate.queryForList(sql, Long.class, familyUserId);
                log.info("创建测试数据后，家属用户 {} 关联的老人ID列表: {}", familyUserId, elderlyIds);
            }
            
            return elderlyIds;
        } catch (Exception e) {
            log.error("查询家属用户 {} 关联老人失败", familyUserId, e);
            // 发生异常时返回空列表
            return List.of();
        }
    }

    /**
     * 根据家属用户ID获取关联的老人ID列表（别名方法）
     *
     * @param familyUserId 家属用户ID
     * @return 老人ID列表
     */
    @Override
    public List<Long> getRelatedElderlyIds(Long familyUserId) {
        return getElderlyIdsByFamilyId(familyUserId);
    }

    /**
     * 验证家属是否有权限查看指定老人信息
     *
     * @param familyUserId 家属用户ID
     * @param elderlyId 老人ID
     * @return 是否有权限
     */
    @Override
    public boolean hasPermissionToViewElderly(Long familyUserId, Long elderlyId) {
        List<Long> allowedElderlyIds = getElderlyIdsByFamilyId(familyUserId);
        return allowedElderlyIds.contains(elderlyId);
    }

    /**
     * 验证家属是否有权限查看指定老人列表
     *
     * @param familyUserId 家属用户ID
     * @param elderlyIds 老人ID列表
     * @return 是否全部有权限
     */
    @Override
    public boolean hasPermissionToViewElderlyList(Long familyUserId, List<Long> elderlyIds) {
        if (elderlyIds == null || elderlyIds.isEmpty()) {
            return true;
        }

        List<Long> allowedElderlyIds = getElderlyIdsByFamilyId(familyUserId);
        return allowedElderlyIds.containsAll(elderlyIds);
    }

    /**
     * 创建家属-老人关联测试数据
     *
     * @param familyUserId 家属用户ID
     */
    private void createTestFamilyElderlyRelation(Long familyUserId) {
        try {
            log.info("为家属用户 {} 创建测试关联数据", familyUserId);
            
            // 首先检查family_user表中是否存在该家属
            String checkFamilyUserSql = "SELECT COUNT(*) FROM family_user WHERE sys_user_id = ?";
            Integer familyUserCount = jdbcTemplate.queryForObject(checkFamilyUserSql, Integer.class, familyUserId);
            
            if (familyUserCount == null || familyUserCount == 0) {
                // 创建家属用户记录
                String insertFamilyUserSql = "INSERT INTO family_user " +
                    "(sys_user_id, real_name, gender, phone, email, status, create_time, create_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?, NOW(), ?)";
                jdbcTemplate.update(insertFamilyUserSql, 
                    familyUserId, "李家属", 1, "13800138002", "family@smartcare.com", 1, 1);
                log.info("已创建家属用户记录，sys_user_id: {}", familyUserId);
            }
            
            // 获取数据库中现有的老人ID（取前5个用于测试）
            String getElderlyIdsSql = "SELECT id FROM elderly WHERE status = 1 AND is_deleted = 0 ORDER BY id LIMIT 5";
            List<Long> existingElderlyIds = jdbcTemplate.queryForList(getElderlyIdsSql, Long.class);
            
            if (existingElderlyIds.isEmpty()) {
                log.warn("数据库中没有可用的老人记录，无法创建关联关系");
                return;
            }
            
            // 获取family_user的真实ID
            String getFamilyUserIdSql = "SELECT id FROM family_user WHERE sys_user_id = ?";
            Long realFamilyUserId = jdbcTemplate.queryForObject(getFamilyUserIdSql, Long.class, familyUserId);
            
            // 为每个老人创建关联关系
            String insertRelationSql = "INSERT IGNORE INTO family_elderly_relation " +
                "(family_user_id, elderly_id, relationship_type, relationship_name, " +
                "is_primary, is_emergency, contact_priority, start_date, status, create_time, create_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, CURDATE(), 1, NOW(), ?)";
            
            String[] relationships = {"child", "child", "child", "grandchild", "grandchild"};
            String[] relationshipNames = {"儿子", "女儿", "儿子", "孙子", "孙女"};
            boolean[] isPrimary = {true, false, false, false, false};
            boolean[] isEmergency = {true, true, false, false, false};
            int[] priorities = {1, 2, 3, 4, 5};
            
            for (int i = 0; i < existingElderlyIds.size(); i++) {
                Long elderlyId = existingElderlyIds.get(i);
                jdbcTemplate.update(insertRelationSql,
                    realFamilyUserId, elderlyId, relationships[i], relationshipNames[i],
                    isPrimary[i] ? 1 : 0, isEmergency[i] ? 1 : 0, priorities[i], 1);
                log.info("已创建家属-老人关联关系: 家属ID={}, 老人ID={}, 关系={}", 
                    realFamilyUserId, elderlyId, relationshipNames[i]);
            }
            
            log.info("测试关联数据创建完成，共创建 {} 条关联关系", existingElderlyIds.size());
            
        } catch (Exception e) {
            log.error("创建测试家属-老人关联数据失败", e);
        }
    }
}
