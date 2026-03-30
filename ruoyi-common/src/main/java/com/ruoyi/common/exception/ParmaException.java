package com.ruoyi.common.exception;

/**
 * @ClassName ParmaException
 * @Description 参数异常类
 * @Author Huang Yx
 * @Date 2026/1/30 1:10
 */
public class ParmaException extends RuntimeException {

    public ParmaException(){}
    public ParmaException(String msg) {
        super(msg);
    }

}
