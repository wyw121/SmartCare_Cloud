package com.smartcare.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartcare.cloud.entity.Elderly;
import org.apache.ibatis.annotations.Mapper;

/**
 * 老人信息Mapper接口
 * 
 * @author SmartCare Team
 * @date 2024-01-01
 */
@Mapper
public interface ElderlyMapper extends BaseMapper<Elderly> {
    
    // MyBatis Plus 已经提供了基础的CRUD操作
    // 可以在这里添加自定义的查询方法
    
}
