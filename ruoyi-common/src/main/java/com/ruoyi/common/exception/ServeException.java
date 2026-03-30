package com.ruoyi.common.exception;

/**
 * @ClassName ParmaException
 * @Description 业务流程异常类
 * @Author Huang Yx
 * @Date 2026/1/30 1:10
 */
public class ServeException extends RuntimeException {

    public ServeException(){}
    public ServeException(String msg) {
        super(msg);
    }

}
