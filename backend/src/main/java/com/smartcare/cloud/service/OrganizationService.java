package com.smartcare.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartcare.cloud.entity.Organization;

/**
 * 机构信息服务接口
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
public interface OrganizationService extends IService<Organization> {

    /**
     * 根据机构编码查询
     * 
     * @param orgCode 机构编码
     * @return 机构信息
     */
    Organization getByOrgCode(String orgCode);

    /**
     * 更新床位使用情况
     * 
     * @param orgId 机构ID
     * @param occupiedBeds 已使用床位数
     * @return 是否成功
     */
    boolean updateOccupiedBeds(Long orgId, Integer occupiedBeds);

    /**
     * 检查床位是否充足
     * 
     * @param orgId 机构ID
     * @param requiredBeds 需要的床位数
     * @return 是否充足
     */
    boolean checkBedAvailability(Long orgId, Integer requiredBeds);
}
