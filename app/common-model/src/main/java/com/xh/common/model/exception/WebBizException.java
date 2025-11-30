package com.xh.common.model.exception;

import lombok.Getter;

@Getter
public class WebBizException extends RuntimeException {

    private final String alertMessage;

    public WebBizException(String alertMessage) {
        this(alertMessage, alertMessage, null);
    }

    public WebBizException(String alertMessage, String message) {
        this(alertMessage, message, null);
    }

    public WebBizException(String alertMessage, String message, Throwable cause) {
        super(message, cause);
        this.alertMessage = alertMessage;
    }
}
