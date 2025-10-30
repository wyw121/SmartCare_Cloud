package com.smartcare.cloud.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartcare.cloud.entity.FamilyUser;

/**
 * 家属用户Mapper接口
 * 
 * 继承MyBatis-Plus的BaseMapper,提供基础CRUD功能
 *
 * @author SmartCare Team
 * @date 2025-10-29
 */
@Mapper
public interface FamilyUserMapper extends BaseMapper<FamilyUser> {
    
    // 基础CRUD方法继承自BaseMapper
    // 自定义查询方法可在此添加,并在对应的XML文件中实现
}
