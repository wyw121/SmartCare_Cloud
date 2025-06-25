package com.smartcare.cloud.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 老人档案分页查询DTO
 *
 * @author GitHub Copilot
 * @since 2025-06-25
 */
@Data
public class ElderlyPageDTO {

    /**
     * 页码，从1开始
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    @NotNull(message = "每页大小不能为空")
    @Min(value = 1, message = "每页大小必须大于0")
    private Integer pageSize = 10;

    /**
     * 姓名（模糊查询）
     */
    private String name;

    /**
     * 性别 (1-男, 0-女)
     */
    private Integer gender;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 健康状况 (HEALTHY-健康, SUBHEALTH-亚健康, SICK-疾病, SERIOUS-重病, DANGER-危险,
     * WARNING-预警)
     */
    private String healthStatus;

    /**
     * 照护等级 (1-自理, 2-半自理, 3-不能自理)
     */
    private Integer careLevel;

    /**
     * 家庭医生ID
     */
    private Long familyDoctorId;

    /**
     * 档案状态 (1-正常, 0-停用)
     */
    private Integer status;

    /**
     * 年龄范围 - 最小年龄
     */
    private Integer minAge;

    /**
     * 年龄范围 - 最大年龄
     */
    private Integer maxAge;

    /**
     * 排序字段
     */
    private String orderBy = "create_time";

    /**
     * 排序方向 (asc/desc)
     */
    private String orderDirection = "desc";

    /**
     * 获取当前页码 (MyBatis Plus 兼容方法)
     */
    public Integer getCurrent() {
        return this.pageNum;
    }

    /**
     * 获取每页大小 (MyBatis Plus 兼容方法)
     */
    public Integer getSize() {
        return this.pageSize;
    }
}
