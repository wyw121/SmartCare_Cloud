package com.smartcare.cloud.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartcare.cloud.entity.Elderly;

/**
 * 老人信息Mapper接口
 *
 * @author SmartCare Team
 * @date 2024-01-01
 */
@Mapper
public interface ElderlyMapper extends BaseMapper<Elderly> {

    /**
     * 自定义分页查询，明确指定查询字段
     */
    @Select("SELECT id, name, id_card, phone, address, gender, birth_date, health_status, "
            + "care_level, room_number, guardian_name, guardian_phone, emergency_contact, emergency_phone, "
            + "create_time, update_time "
            + "FROM elderly WHERE is_deleted = 0 ORDER BY create_time DESC")
    Page<Elderly> selectElderlyPage(Page<Elderly> page);
}
