package com.wbtkj.chat.exception;

// 服务器异常
public class MyException extends RuntimeException{
    private int code = 500;

    public MyException() {
        super("请求出错，请联系管理员");
    }
    public MyException(String message) {
        super(message);
    }
    public MyException(String message, Throwable cause) {
        super(message, cause);
    }
    public MyException(Throwable cause) {
        super(cause);
    }
    public MyException(String message, Throwable cause,
                              boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    public MyException(int code) {
        this.code = code;
    }
    public MyException(String message, int code) {
        super(message);
        this.code = code;
    }
    public MyException(String message, Throwable cause,
                              int code) {
        super(message, cause);
        this.code = code;
    }
    public MyException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }
    public MyException(String message, Throwable cause,
                              boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}