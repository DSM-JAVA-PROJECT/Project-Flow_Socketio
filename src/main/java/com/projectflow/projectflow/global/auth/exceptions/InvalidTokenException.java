package com.projectflow.projectflow.global.auth.exceptions;

import com.projectflow.projectflow.global.exception.ErrorCode;
import com.projectflow.projectflow.global.exception.GlobalException;

public class InvalidTokenException extends GlobalException {
    public static final GlobalException EXCEPTION = new InvalidTokenException();

    private InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
