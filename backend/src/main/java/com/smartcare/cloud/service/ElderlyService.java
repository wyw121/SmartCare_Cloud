package com.smartcare.cloud.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartcare.cloud.dto.ElderlyPageDTO;
import com.smartcare.cloud.entity.Elderly;
import com.smartcare.cloud.vo.ResponseResult;

import java.util.List;

/**
 * 老人信息服务接口
 *
 * @author SmartCare Team
 * @date 2024-01-01
 */
public interface ElderlyService {

    /**
     * 分页查询老人信息
     *
     * @param pageDTO 分页查询参数
     * @return 分页结果
     */
    ResponseResult<Page<Elderly>> getElderlyPage(ElderlyPageDTO pageDTO);

    /**
     * 根据ID获取老人信息
     *
     * @param id 老人ID
     * @return 老人信息
     */
    ResponseResult<Elderly> getElderlyById(Long id);

    /**
     * 新增老人信息
     *
     * @param elderly 老人信息
     * @return 操作结果
     */
    ResponseResult<Void> addElderly(Elderly elderly);

    /**
     * 更新老人信息
     *
     * @param elderly 老人信息
     * @return 操作结果
     */
    ResponseResult<Void> updateElderly(Elderly elderly);

    /**
     * 删除老人信息
     *
     * @param id 老人ID
     * @return 操作结果
     */
    ResponseResult<Void> deleteElderly(Long id);

    /**
     * 批量删除老人信息
     *
     * @param ids 老人ID列表
     * @return 操作结果
     */
    ResponseResult<Void> batchDeleteElderly(List<Long> ids);

    /**
     * 获取重点关注老人列表
     *
     * @return 重点关注老人列表
     */
    ResponseResult<List<Elderly>> getKeyElderlyList();

    /**
     * 根据健康状态统计老人数量
     *
     * @return 统计结果
     */
    ResponseResult<Object> getElderlyHealthStatistics();
}
