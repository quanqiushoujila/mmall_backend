package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
// 序列化json,如果对象为null,则该对象key值消失
public class ServiceResponse<T> implements Serializable {
    // 状态码
    private int status;
    // 状态信息
    private String msg;
    // 所属对象类型
    private T data;

    private ServiceResponse(int status) {
        this.status = status;
    }

    private ServiceResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServiceResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServiceResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus () {
        return status;
    }
    public String getMsg () {
        return msg;
    }

    public T getData () {
        return data;
    }
    @JsonIgnore
    public Boolean isSuccess () {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public static <T> ServiceResponse<T> createdBySuccess () {
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServiceResponse<T> createdBySuccessMessage (String msg) {
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServiceResponse<T> createdBySuccess (T data) {
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServiceResponse<T> createdBySuccess (String msg, T data) {
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> ServiceResponse<T> createdByError () {
        return new ServiceResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServiceResponse<T> createdByErrorMessage (String errorMessage) {
        return new ServiceResponse<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ServiceResponse<T> createdByeErrorCodeMessage (int errorCode, String errorMessage) {
        return new ServiceResponse<T>(errorCode, errorMessage);
    }


}
