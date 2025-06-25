package com.smartcare.cloud.controller;

import com.github.pagehelper.PageInfo;
import com.smartcare.cloud.dto.DoctorPageDTO;
import com.smartcare.cloud.entity.Doctor;
import com.smartcare.cloud.service.DoctorService;
import com.smartcare.cloud.vo.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
}
