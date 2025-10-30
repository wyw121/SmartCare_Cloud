package com.smartcare.cloud.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartcare.cloud.entity.NurseInfo;
import com.smartcare.cloud.mapper.NurseElderlyRelationMapper;
import com.smartcare.cloud.mapper.NurseInfoMapper;
import com.smartcare.cloud.service.NurseInfoService;

/**
 * 护工信息服务实现类
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Service
public class NurseInfoServiceImpl extends ServiceImpl<NurseInfoMapper, NurseInfo> implements NurseInfoService {

    @Autowired
    private NurseElderlyRelationMapper nurseElderlyRelationMapper;

    @Override
    public NurseInfo getByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        
        QueryWrapper<NurseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("is_deleted", 0);
        
        return this.getOne(queryWrapper);
    }

    @Override
    public NurseInfo getByEmployeeNumber(String employeeNumber) {
        if (employeeNumber == null || employeeNumber.trim().isEmpty()) {
            return null;
        }
        
        QueryWrapper<NurseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_number", employeeNumber)
                   .eq("is_deleted", 0);
        
        return this.getOne(queryWrapper);
    }

    @Override
    public int countCurrentElderly(Long nurseId) {
        if (nurseId == null) {
            return 0;
        }
        
        return nurseElderlyRelationMapper.countCurrentElderlyByNurseId(nurseId);
    }

    @Override
    public boolean isMaxCapacityReached(Long nurseId) {
        if (nurseId == null) {
            return false;
        }
        
        NurseInfo nurse = this.getById(nurseId);
        if (nurse == null) {
            return false;
        }
        
        int currentCount = this.countCurrentElderly(nurseId);
        Integer maxCareCount = nurse.getMaxCareCount();
        
        return maxCareCount != null && currentCount >= maxCareCount;
    }
}
