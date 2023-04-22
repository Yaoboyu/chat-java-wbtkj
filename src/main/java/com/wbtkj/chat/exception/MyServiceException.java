package com.wbtkj.chat.exception;

// 客户端业务异常
public class MyServiceException extends RuntimeException{
    private int code = 400;

    public MyServiceException() { }
    public MyServiceException(String message) {
        super(message);
    }
    public MyServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public MyServiceException(Throwable cause) {
        super(cause);
    }
    public MyServiceException(String message, Throwable cause,
                            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    public MyServiceException(int code) {
        this.code = code;
    }
    public MyServiceException(String message, int code) {
        super(message);
        this.code = code;
    }
    public MyServiceException(String message, Throwable cause,
                            int code) {
        super(message, cause);
        this.code = code;
    }
    public MyServiceException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }
    public MyServiceException(String message, Throwable cause,
                            boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}