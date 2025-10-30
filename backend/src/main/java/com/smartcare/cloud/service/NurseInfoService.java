package com.smartcare.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartcare.cloud.entity.NurseInfo;

/**
 * 护工信息服务接口
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
public interface NurseInfoService extends IService<NurseInfo> {

    /**
     * 根据用户ID查询护工信息
     * 
     * @param userId 系统用户ID
     * @return 护工信息
     */
    NurseInfo getByUserId(Long userId);

    /**
     * 根据工号查询护工
     * 
     * @param employeeNumber 工号
     * @return 护工信息
     */
    NurseInfo getByEmployeeNumber(String employeeNumber);

    /**
     * 统计护工当前照护人数
     * 
     * @param nurseId 护工ID
     * @return 照护人数
     */
    int countCurrentElderly(Long nurseId);

    /**
     * 检查护工是否达到最大照护人数
     * 
     * @param nurseId 护工ID
     * @return 是否已满
     */
    boolean isMaxCapacityReached(Long nurseId);
}
