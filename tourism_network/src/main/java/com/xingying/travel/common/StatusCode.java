package com.xingying.travel.common;

/**
 * @Title: 状态码实体类
 * @author: 陈宏松
 * @create: 2018-12-18 14:37
 * @version: 1.0.0
 **/
public class StatusCode {

    public static final int SMS = 0;//发送成功
    public static final int OK = 0;//成功
    public static final int ERROR = 201;//失败
    public static final int LOGINERROR = 202;//用户名密码错误
    public static final int ACCESSERROR = 203;//权限不足
    public static final int REMOTEERROR = 204;//远程调用失败
    public static final int REPERROR = 205;//重复操作

}
