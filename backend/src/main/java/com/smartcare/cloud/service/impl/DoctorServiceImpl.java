package com.smartcare.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smartcare.cloud.dto.DoctorPageDTO;
import com.smartcare.cloud.entity.Doctor;
import com.smartcare.cloud.mapper.DoctorMapper;
import com.smartcare.cloud.service.DoctorService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医生服务实现类
 * 
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements DoctorService {

    @Override
    public PageInfo<Doctor> getPageList(DoctorPageDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        
        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();
        
        // 姓名模糊查询
        if (StringUtils.hasText(dto.getName())) {
            queryWrapper.like("name", dto.getName());
        }
        
        // 工号精确查询
        if (StringUtils.hasText(dto.getEmployeeNumber())) {
            queryWrapper.eq("employee_number", dto.getEmployeeNumber());
        }
        
        // 科室精确查询
        if (StringUtils.hasText(dto.getDepartment())) {
            queryWrapper.eq("department", dto.getDepartment());
        }
        
        // 职称精确查询
        if (StringUtils.hasText(dto.getTitle())) {
            queryWrapper.eq("title", dto.getTitle());
        }
        
        // 专长模糊查询
        if (StringUtils.hasText(dto.getSpecialization())) {
            queryWrapper.like("specialization", dto.getSpecialization());
        }
        
        // 状态查询
        if (dto.getStatus() != null) {
            queryWrapper.eq("status", dto.getStatus());
        }
        
        // 医院ID查询
        if (dto.getHospitalId() != null) {
            queryWrapper.eq("hospital_id", dto.getHospitalId());
        }
        
        // 按创建时间倒序
        queryWrapper.orderByDesc("create_time");
        
        List<Doctor> list = this.list(queryWrapper);
        return new PageInfo<>(list);
    }

    @Override
    public List<Doctor> getByDepartment(String department) {
        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("department", department)
                   .eq("status", 1)
                   .orderBy(true, true, "title", "name");
        return this.list(queryWrapper);
    }

    @Override
    public List<Doctor> getByHospitalId(Long hospitalId) {
        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hospital_id", hospitalId)
                   .eq("status", 1)
                   .orderBy(true, true, "department", "title", "name");
        return this.list(queryWrapper);
    }

    @Override
    public Doctor getByEmployeeNumber(String employeeNumber) {
        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_number", employeeNumber);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return this.removeByIds(ids);
    }

    @Override
    public List<Object> getDepartmentStatistics() {
        return baseMapper.getDepartmentStatistics();
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setStatus(status);
        doctor.setUpdateTime(LocalDateTime.now());
        return this.updateById(doctor);
    }
    
    @Override
    public boolean save(Doctor entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return super.save(entity);
    }
    
    @Override
    public boolean updateById(Doctor entity) {
        entity.setUpdateTime(LocalDateTime.now());
        return super.updateById(entity);
    }
}
