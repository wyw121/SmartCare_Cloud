package com.smartcare.cloud.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcare.cloud.entity.Organization;
import com.smartcare.cloud.mapper.OrganizationMapper;
import com.smartcare.cloud.service.OrganizationService;

/**
 * 机构信息服务实现类
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> 
        implements OrganizationService {

    @Override
    public Organization getByOrgCode(String orgCode) {
        if (orgCode == null || orgCode.trim().isEmpty()) {
            return null;
        }
        
        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("org_code", orgCode)
                   .eq("is_deleted", 0);
        
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean updateOccupiedBeds(Long orgId, Integer occupiedBeds) {
        if (orgId == null || occupiedBeds == null) {
            return false;
        }
        
        Organization org = this.getById(orgId);
        if (org == null) {
            return false;
        }
        
        org.setOccupiedBeds(occupiedBeds);
        return this.updateById(org);
    }

    @Override
    public boolean checkBedAvailability(Long orgId, Integer requiredBeds) {
        if (orgId == null || requiredBeds == null) {
            return false;
        }
        
        Organization org = this.getById(orgId);
        if (org == null) {
            return false;
        }
        
        Integer totalBeds = org.getTotalBeds();
        Integer occupiedBeds = org.getOccupiedBeds();
        
        if (totalBeds == null || occupiedBeds == null) {
            return false;
        }
        
        int availableBeds = totalBeds - occupiedBeds;
        return availableBeds >= requiredBeds;
    }
}
