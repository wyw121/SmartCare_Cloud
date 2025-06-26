package com.smartcare.cloud.vo;

/**
 * 统一响应结果类
 *
 * @author GitHub Copilot
 * @since 2025-06-25
 */
public class ResponseResult<T> {

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    public ResponseResult() {
        this.timestamp = System.currentTimeMillis();
    }

    public ResponseResult(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public ResponseResult(Integer code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(200, "操作成功");
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(200, "操作成功", data);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> ResponseResult<T> success(String message, T data) {
        return new ResponseResult<>(200, message, data);
    }

    /**
     * 失败响应
     */
    public static <T> ResponseResult<T> error() {
        return new ResponseResult<>(500, "操作失败");
    }

    /**
     * 失败响应（自定义消息）
     */
    public static <T> ResponseResult<T> error(String message) {
        return new ResponseResult<>(500, message);
    }

    /**
     * 失败响应（自定义状态码和消息）
     */
    public static <T> ResponseResult<T> error(Integer code, String message) {
        return new ResponseResult<>(code, message);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return this.code != null && this.code == 200;
    }

    // 手动添加getter/setter方法
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
}
