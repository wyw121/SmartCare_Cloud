package com.smartcare.cloud.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.smartcare.cloud.dto.DoctorPageDTO;
import com.smartcare.cloud.entity.Doctor;
import com.smartcare.cloud.vo.DoctorStatisticsVO;

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

    /**
     * 获取医生统计信息
     *
     * @param doctorId 医生ID
     * @return 统计信息
     */
    DoctorStatisticsVO getDoctorStatistics(Long doctorId);

    /**
     * 分配老人给医生
     *
     * @param doctorId 医生ID
     * @param elderlyIds 老人ID列表
     * @return 分配结果
     */
    boolean assignElderlyToDoctor(Long doctorId, List<Long> elderlyIds);

    /**
     * 获取医生负责的老人列表
     *
     * @param doctorId 医生ID
     * @return 老人列表
     */
    List<Object> getDoctorElderlyList(Long doctorId);

    /**
     * 获取科室医生工作负荷统计
     *
     * @param department 科室名称
     * @return 工作负荷统计
     */
    List<Object> getDepartmentWorkload(String department);

    /**
     * 获取医生排班信息
     *
     * @param doctorId 医生ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 排班信息
     */
    List<Object> getDoctorSchedule(Long doctorId, String startDate, String endDate);

    /**
     * 导出医生数据
     *
     * @param ids 医生ID列表（可选）
     * @return 导出数据
     */
    Object exportDoctorData(List<Long> ids);

    /**
     * 批量导入医生数据
     *
     * @param importData 导入数据
     * @return 导入结果
     */
    Object importDoctorData(Object importData);
}
