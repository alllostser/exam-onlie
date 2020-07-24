package org.springblade.common.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口向前端响应对象
 */
@Data
public class ServerResponse<T> implements Serializable {
    private  int status;//接口返回的状态码 0：代表调用接口成功。 非0调用接口失败
    private String msg; //当调用失败时，封装错误信息
    private T data; //


    private ServerResponse(){

    }
    private ServerResponse(int status){
        this.status=status;
    }
    private ServerResponse(int status, T data){
        this.status=status;
        this.data=data;
    }
    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    private ServerResponse(String msg) {
        this.msg = msg;
    }
    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(T data) {
        this.data = data;
    }
    /**
     * 当接口调用成功
     */
    public static ServerResponse serverResponseBySucess(){
        return new ServerResponse(0);
    }
    public static <T> ServerResponse serverResponseBySucess(T data){
        return new ServerResponse(0,data);
    }
    public static <T> ServerResponse serverResponseBySucess(String msg, T data){
        return new ServerResponse(0,msg,data);
    }
    public static ServerResponse serverResponseBySucess(String msg){
        return new ServerResponse(0,msg);
    }
    /**
     * 接口调用失败
     */
    public static <T> ServerResponse serverResponseByFail(int status, String msg){
        return new ServerResponse(status,msg);
    }
    /**
     * 判断一个接口是否调用成功
     */
    public boolean isSucess(){
        return this.status==0;
    }
}
