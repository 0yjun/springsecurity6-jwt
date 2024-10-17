package com.security.common.exception;

public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomException(ErrorCode v){
        super(v.getMessgae());
        this.errorCode = v;
    };

    public CustomException(ErrorCode v, String message){
        super(v.getMessgae() + message);
        this.errorCode = v;
    }


}
