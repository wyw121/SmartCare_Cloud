package com.smartcare.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartcare.cloud.entity.Doctor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 医生数据访问接口
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {

    /**
     * 统计各科室医生数量
     *
     * @return 统计结果
     */
    @Select("SELECT department as name, COUNT(*) as count FROM t_doctor WHERE status = 1 GROUP BY department ORDER BY count DESC")
    List<Object> getDepartmentStatistics();
}
