package com.xingying.travel.common;

/**
 * @Title: 控制器类返回结果
 * @author: 陈宏松
 * @create: 2018-12-18 14:28
 * @version: 1.0.0
 **/
public class Result {

    private boolean flag; //是否成功
    private Integer code; //返回码
    private String msg; //返回信息
    private Object data; //返回数据

    public Result() {
    }

    public Result(boolean flag, Integer code, String msg, Object data) {
        this.flag = flag;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(boolean flag, Integer code, String msg) {
        this.flag = flag;
        this.code = code;
        this.msg = msg;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
