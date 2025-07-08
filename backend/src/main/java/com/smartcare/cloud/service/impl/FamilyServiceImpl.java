package com.smartcare.cloud.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * 根据家属用户ID获取关联的老人ID列表
     *
     * @param familyUserId 家属用户ID
     * @return 老人ID列表
     */
    @Override
    public List<Long> getElderlyIdsByFamilyId(Long familyUserId) {
        // TODO: 实现数据库查询
        // 目前使用硬编码的测试数据
        if (familyUserId == 3L) { // 家属用户ID为3
            return List.of(1L, 2L); // 关联老人ID 1和2
        }
        return List.of();
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
}
