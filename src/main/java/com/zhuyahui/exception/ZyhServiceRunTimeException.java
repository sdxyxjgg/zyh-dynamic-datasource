package com.zhuyahui.exception;

/**
 * 这个异常是为了service出现错误，进行事务回滚使用
 *
 * @author : Zhu Yahui
 * @version : 1.0.0
 * @date : 2023/1/2
 */
public class ZyhServiceRunTimeException extends RuntimeException {
    private static final long serialVersionUID = 7647654407667242222L;

    public ZyhServiceRunTimeException(String message) {
        super(message);
    }

    public ZyhServiceRunTimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
