package com.smartcare.cloud.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.smartcare.cloud.dto.DoctorPageDTO;
import com.smartcare.cloud.entity.Doctor;
import com.smartcare.cloud.service.DoctorService;
import com.smartcare.cloud.vo.DoctorStatisticsVO;
import com.smartcare.cloud.vo.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 医生管理控制器
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Tag(name = "医生管理", description = "医生信息管理相关接口")
@RestController
@RequestMapping("/api/doctor")
@CrossOrigin
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * 分页查询医生列表
     *
     * @param dto 查询条件
     * @return 分页结果
     */
    @Operation(summary = "分页查询医生列表")
    @PostMapping("/page")
    public ResponseResult<PageInfo<Doctor>> getPageList(@RequestBody DoctorPageDTO dto) {
        try {
            PageInfo<Doctor> pageInfo = doctorService.getPageList(dto);
            return ResponseResult.success(pageInfo);
        } catch (Exception e) {
            return ResponseResult.error("查询医生列表失败: " + e.getMessage());
        }
    }

    /**
     * 新增医生
     *
     * @param doctor 医生信息
     * @return 操作结果
     */
    @Operation(summary = "新增医生")
    @PostMapping("/add")
    public ResponseResult<String> add(@Valid @RequestBody Doctor doctor) {
        try {
            // 检查工号是否重复
            Doctor existing = doctorService.getByEmployeeNumber(doctor.getEmployeeNumber());
            if (existing != null) {
                return ResponseResult.error("医生工号已存在");
            }

            boolean result = doctorService.save(doctor);
            if (result) {
                return ResponseResult.success("新增医生成功");
            } else {
                return ResponseResult.error("新增医生失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("新增医生失败: " + e.getMessage());
        }
    }

    /**
     * 更新医生信息
     *
     * @param doctor 医生信息
     * @return 操作结果
     */
    @Operation(summary = "更新医生信息")
    @PutMapping("/update")
    public ResponseResult<String> update(@Valid @RequestBody Doctor doctor) {
        try {
            boolean result = doctorService.updateById(doctor);
            if (result) {
                return ResponseResult.success("更新医生信息成功");
            } else {
                return ResponseResult.error("更新医生信息失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新医生信息失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询医生详情
     *
     * @param id 医生ID
     * @return 医生详情
     */
    @Operation(summary = "查询医生详情")
    @GetMapping("/{id}")
    public ResponseResult<Doctor> getById(@PathVariable Long id) {
        try {
            Doctor doctor = doctorService.getById(id);
            if (doctor != null) {
                return ResponseResult.success(doctor);
            } else {
                return ResponseResult.error("医生不存在");
            }
        } catch (Exception e) {
            return ResponseResult.error("查询医生详情失败: " + e.getMessage());
        }
    }

    /**
     * 删除医生
     *
     * @param id 医生ID
     * @return 操作结果
     */
    @Operation(summary = "删除医生")
    @DeleteMapping("/{id}")
    public ResponseResult<String> delete(@PathVariable Long id) {
        try {
            boolean result = doctorService.removeById(id);
            if (result) {
                return ResponseResult.success("删除医生成功");
            } else {
                return ResponseResult.error("删除医生失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("删除医生失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除医生
     *
     * @param ids 医生ID列表
     * @return 操作结果
     */
    @Operation(summary = "批量删除医生")
    @DeleteMapping("/batch")
    public ResponseResult<String> deleteBatch(@RequestBody List<Long> ids) {
        try {
            boolean result = doctorService.deleteBatch(ids);
            if (result) {
                return ResponseResult.success("批量删除医生成功");
            } else {
                return ResponseResult.error("批量删除医生失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("批量删除医生失败: " + e.getMessage());
        }
    }

    /**
     * 根据科室查询医生列表
     *
     * @param department 科室
     * @return 医生列表
     */
    @Operation(summary = "根据科室查询医生列表")
    @GetMapping("/department/{department}")
    public ResponseResult<List<Doctor>> getByDepartment(@PathVariable String department) {
        try {
            List<Doctor> doctors = doctorService.getByDepartment(department);
            return ResponseResult.success(doctors);
        } catch (Exception e) {
            return ResponseResult.error("查询医生列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据医院ID查询医生列表
     *
     * @param hospitalId 医院ID
     * @return 医生列表
     */
    @Operation(summary = "根据医院ID查询医生列表")
    @GetMapping("/hospital/{hospitalId}")
    public ResponseResult<List<Doctor>> getByHospitalId(@PathVariable Long hospitalId) {
        try {
            List<Doctor> doctors = doctorService.getByHospitalId(hospitalId);
            return ResponseResult.success(doctors);
        } catch (Exception e) {
            return ResponseResult.error("查询医生列表失败: " + e.getMessage());
        }
    }

    /**
     * 统计各科室医生数量
     *
     * @return 统计结果
     */
    @Operation(summary = "统计各科室医生数量")
    @GetMapping("/statistics/department")
    public ResponseResult<List<Object>> getDepartmentStatistics() {
        try {
            List<Object> statistics = doctorService.getDepartmentStatistics();
            return ResponseResult.success(statistics);
        } catch (Exception e) {
            return ResponseResult.error("统计科室医生数量失败: " + e.getMessage());
        }
    }

    /**
     * 更新医生状态
     *
     * @param id 医生ID
     * @param status 状态
     * @return 操作结果
     */
    @Operation(summary = "更新医生状态")
    @PutMapping("/{id}/status/{status}")
    public ResponseResult<String> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        try {
            boolean result = doctorService.updateStatus(id, status);
            if (result) {
                return ResponseResult.success("更新医生状态成功");
            } else {
                return ResponseResult.error("更新医生状态失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新医生状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取医生统计信息
     *
     * @param id 医生ID
     * @return 统计信息
     */
    @Operation(summary = "获取医生统计信息")
    @GetMapping("/{id}/statistics")
    public ResponseResult<DoctorStatisticsVO> getDoctorStatistics(@PathVariable Long id) {
        try {
            DoctorStatisticsVO statistics = doctorService.getDoctorStatistics(id);
            return ResponseResult.success(statistics);
        } catch (Exception e) {
            return ResponseResult.error("获取医生统计信息失败: " + e.getMessage());
        }
    }

    /**
     * 分配老人给医生
     *
     * @param doctorId 医生ID
     * @param elderlyIds 老人ID列表
     * @return 操作结果
     */
    @Operation(summary = "分配老人给医生")
    @PostMapping("/{doctorId}/assign-elderly")
    public ResponseResult<String> assignElderlyToDoctor(
            @PathVariable Long doctorId,
            @RequestBody List<Long> elderlyIds) {
        try {
            boolean result = doctorService.assignElderlyToDoctor(doctorId, elderlyIds);
            if (result) {
                return ResponseResult.success("分配老人成功");
            } else {
                return ResponseResult.error("分配老人失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("分配老人失败: " + e.getMessage());
        }
    }

    /**
     * 获取医生负责的老人列表
     *
     * @param doctorId 医生ID
     * @return 老人列表
     */
    @Operation(summary = "获取医生负责的老人列表")
    @GetMapping("/{doctorId}/elderly")
    public ResponseResult<List<Object>> getDoctorElderlyList(@PathVariable Long doctorId) {
        try {
            List<Object> elderlyList = doctorService.getDoctorElderlyList(doctorId);
            return ResponseResult.success(elderlyList);
        } catch (Exception e) {
            return ResponseResult.error("获取老人列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取科室医生工作负荷统计
     *
     * @param department 科室名称
     * @return 工作负荷统计
     */
    @Operation(summary = "获取科室医生工作负荷统计")
    @GetMapping("/department/{department}/workload")
    public ResponseResult<List<Object>> getDepartmentWorkload(@PathVariable String department) {
        try {
            List<Object> workload = doctorService.getDepartmentWorkload(department);
            return ResponseResult.success(workload);
        } catch (Exception e) {
            return ResponseResult.error("获取工作负荷统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取医生排班信息
     *
     * @param doctorId 医生ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 排班信息
     */
    @Operation(summary = "获取医生排班信息")
    @GetMapping("/{doctorId}/schedule")
    public ResponseResult<List<Object>> getDoctorSchedule(
            @PathVariable Long doctorId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            List<Object> schedule = doctorService.getDoctorSchedule(doctorId, startDate, endDate);
            return ResponseResult.success(schedule);
        } catch (Exception e) {
            return ResponseResult.error("获取排班信息失败: " + e.getMessage());
        }
    }

    /**
     * 导出医生数据
     *
     * @param ids 医生ID列表（可选）
     * @return 导出结果
     */
    @Operation(summary = "导出医生数据")
    @PostMapping("/export")
    public ResponseResult<Object> exportDoctorData(@RequestBody(required = false) List<Long> ids) {
        try {
            Object exportData = doctorService.exportDoctorData(ids);
            return ResponseResult.success(exportData);
        } catch (Exception e) {
            return ResponseResult.error("导出医生数据失败: " + e.getMessage());
        }
    }

    /**
     * 批量导入医生数据
     *
     * @param importData 导入数据
     * @return 导入结果
     */
    @Operation(summary = "批量导入医生数据")
    @PostMapping("/import")
    public ResponseResult<Object> importDoctorData(@RequestBody Object importData) {
        try {
            Object result = doctorService.importDoctorData(importData);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.error("导入医生数据失败: " + e.getMessage());
        }
    }
}
