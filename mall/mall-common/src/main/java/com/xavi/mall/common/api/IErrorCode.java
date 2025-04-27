package com.xavi.mall.common.api;

/**
 * API返回码接口
 * Created by xavier 
 */
public interface IErrorCode {
    /**
     * 返回码
     */
    long getCode();

    /**
     * 返回信息
     */
    String getMessage();
}
