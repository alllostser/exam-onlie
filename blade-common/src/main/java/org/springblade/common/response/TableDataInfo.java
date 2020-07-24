package org.springblade.common.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TableDataInfo
 * @Description //TODO
 * @Author GuXinYu
 * @Date 2020/5/22 20:36
 * @Version 1.0
 **/
@Data
public class TableDataInfo<T> implements Serializable {
    /**
     * 总记录数
     */
    private Long count;
    /**
     * 列表数据
     */
    private T data;
    /**
     * 消息状态码
     */
    private Integer code;
    /**
     *信息
     * */
    private String msg;

    public TableDataInfo() {
    }
    public TableDataInfo(Integer code, String msg, long count, T data) {
        this.code=code;
        this.msg=msg;
        this.count=count;
        this.data=data;
    }
    public TableDataInfo(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    /**
     * 当接口调用成功
     */
    public static <T> TableDataInfo ResponseBySucess(String msg, Long count, T data){
        return new TableDataInfo(0,msg,count,data);
    }
    /**
     * 接口调用失败
     */
    public static TableDataInfo ResponseByFail(Integer code, String msg){
        return new TableDataInfo(code,msg);
    }
    /**
     * 判断一个接口是否调用成功
     */
    public boolean isSucess(){
        return this.code==0;
    }

}
