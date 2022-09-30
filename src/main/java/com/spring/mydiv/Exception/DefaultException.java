package com.spring.mydiv.Exception;

import com.spring.mydiv.Code.ErrorCode;
import lombok.Getter;

@Getter
public class DefaultException extends RuntimeException{
    private ErrorCode errorCode;
    private String detailMessage;

    public DefaultException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailMessage = errorCode.getMessage();
    }

    public DefaultException(ErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }
}
