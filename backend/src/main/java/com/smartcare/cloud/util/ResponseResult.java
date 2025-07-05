package com.smartcare.cloud.util;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 统一响应结果封装类
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Schema(description = "统一响应结果")
public class ResponseResult<T> {

    @Schema(description = "响应码：200-成功，其他-失败")
    private Integer code;

    @Schema(description = "响应消息")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    @Schema(description = "时间戳")
    private Long timestamp;

    public ResponseResult() {
        this.timestamp = System.currentTimeMillis();
    }

    public ResponseResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(200, "操作成功", data);
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @param message 消息
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success(T data, String message) {
        return new ResponseResult<>(200, message, data);
    }

    /**
     * 成功响应（无数据）
     *
     * @param message 消息
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success(String message) {
        return new ResponseResult<>(200, message, null);
    }

    /**
     * 失败响应
     *
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> error(String message) {
        return new ResponseResult<>(500, message, null);
    }

    /**
     * 失败响应
     *
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> error(Integer code, String message) {
        return new ResponseResult<>(code, message, null);
    }

    /**
     * 失败响应
     *
     * @param code 错误码
     * @param message 错误消息
     * @param data 数据
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> error(Integer code, String message, T data) {
        return new ResponseResult<>(code, message, data);
    }

    // Getters and Setters
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ResponseResult{"
                + "code=" + code
                + ", message='" + message + '\''
                + ", data=" + data
                + ", timestamp=" + timestamp
                + '}';
    }
}
