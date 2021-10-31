package com.projectflow.projectflow.global.exception;

import lombok.Getter;

@Getter
public class SocketException extends RuntimeException {
    private final SocketErrorCode errorCode;
    public SocketException(SocketErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
