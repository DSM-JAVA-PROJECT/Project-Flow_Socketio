package com.projectflow.projectflow.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocketErrorCode {

    JWT_VALIDATE_FAILED("Jwt Validate Failed", 401);

    private final String message;
    private final Integer status;

}
