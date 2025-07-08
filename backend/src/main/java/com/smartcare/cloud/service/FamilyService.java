package com.smartcare.cloud.service;

import java.util.List;

/**
 * 家属服务接口
 *
 * @author SmartCare Team
 * @date 2025-07-09
 */
public interface FamilyService {

    /**
     * 根据家属用户ID获取关联的老人ID列表
     *
     * @param familyUserId 家属用户ID
     * @return 老人ID列表
     */
    List<Long> getElderlyIdsByFamilyId(Long familyUserId);

    /**
     * 验证家属是否有权限查看指定老人信息
     *
     * @param familyUserId 家属用户ID
     * @param elderlyId 老人ID
     * @return 是否有权限
     */
    boolean hasPermissionToViewElderly(Long familyUserId, Long elderlyId);

    /**
     * 验证家属是否有权限查看指定老人列表
     *
     * @param familyUserId 家属用户ID
     * @param elderlyIds 老人ID列表
     * @return 是否全部有权限
     */
    boolean hasPermissionToViewElderlyList(Long familyUserId, List<Long> elderlyIds);
}
