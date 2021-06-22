package cn.kzm.common.dto;

import lombok.Data;

@Data
public class Result<T> {

    /**
     * 成功标志
     */
    private boolean success;

    /**
     * 失败消息
     */
    private String message;

    /**
     * 返回code
     */
    private Integer code;

    /**
     * 接口时间戳
     */
    private long timestamp=System.currentTimeMillis();

    /**
     * 数据
     */
    private T data;



}
