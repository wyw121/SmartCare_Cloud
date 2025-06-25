package com.smartcare.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartcare.cloud.entity.Doctor;
import com.smartcare.cloud.dto.DoctorPageDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 医生服务接口
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
public interface DoctorService extends IService<Doctor> {

    /**
     * 分页查询医生列表
     *
     * @param dto 查询条件
     * @return 分页结果
     */
    PageInfo<Doctor> getPageList(DoctorPageDTO dto);

    /**
     * 根据科室查询医生列表
     *
     * @param department 科室
     * @return 医生列表
     */
    List<Doctor> getByDepartment(String department);

    /**
     * 根据医院ID查询医生列表
     *
     * @param hospitalId 医院ID
     * @return 医生列表
     */
    List<Doctor> getByHospitalId(Long hospitalId);

    /**
     * 根据医生工号查询医生信息
     *
     * @param employeeNumber 医生工号
     * @return 医生信息
     */
    Doctor getByEmployeeNumber(String employeeNumber);

    /**
     * 批量删除医生
     *
     * @param ids 医生ID列表
     * @return 删除结果
     */
    boolean deleteBatch(List<Long> ids);

    /**
     * 统计各科室医生数量
     *
     * @return 统计结果
     */
    List<Object> getDepartmentStatistics();

    /**
     * 更新医生状态
     *
     * @param id 医生ID
     * @param status 状态
     * @return 更新结果
     */
    boolean updateStatus(Long id, Integer status);
}
