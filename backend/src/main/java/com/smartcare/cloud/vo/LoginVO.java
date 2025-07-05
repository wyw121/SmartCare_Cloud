package com.smartcare.cloud.vo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 登录响应VO
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Schema(description = "登录响应信息")
public class LoginVO {

    @Schema(description = "访问令牌")
    private String token;

    @Schema(description = "用户信息")
    private UserInfoVO userInfo;

    @Schema(description = "权限列表")
    private List<String> permissions;

    @Schema(description = "令牌类型")
    private String tokenType = "Bearer";

    @Schema(description = "过期时间（秒）")
    private Long expiresIn;

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfoVO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoVO userInfo) {
        this.userInfo = userInfo;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
