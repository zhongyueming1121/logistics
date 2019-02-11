package com.logts.common.result;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * DyResult
 * @author ymz
 * @date 2018/9/8 11:38
 */
public class DyResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String SUCCESS = "SUCCESS";
    public static final int SUCCESS_CODE = 200;
    public static final String ERROR = "ERROR";
    public static final int ERROR_CODE = 100;

    private Integer status;
    private String msg;
    private T data;

    public DyResult(){
    }

    public DyResult(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public DyResult(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 是否成功 true 成功(status=200)
     * @return
     */
    @JsonIgnore
    public boolean isSuccessFul(){
        return this.status != null && SUCCESS_CODE == this.status;
    }

    @Override
    public String toString() {
        return "DyResult{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

