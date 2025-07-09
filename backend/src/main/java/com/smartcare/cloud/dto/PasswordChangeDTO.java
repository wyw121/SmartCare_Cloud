package com.smartcare.cloud.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 密码修改DTO
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Schema(description = "密码修改DTO")
public class PasswordChangeDTO {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "原密码")
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    @Schema(description = "新密码")
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "新密码长度必须在6-20个字符之间")
    private String newPassword;

    @Schema(description = "确认新密码")
    @NotBlank(message = "确认新密码不能为空")
    private String confirmPassword;

    // 构造方法
    public PasswordChangeDTO() {
        // 默认构造方法
    }

    // Getter和Setter方法
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "PasswordChangeDTO{"
                + "userId=" + userId
                + ", oldPassword='[HIDDEN]'"
                + ", newPassword='[HIDDEN]'"
                + ", confirmPassword='[HIDDEN]'"
                + '}';
    }
}
