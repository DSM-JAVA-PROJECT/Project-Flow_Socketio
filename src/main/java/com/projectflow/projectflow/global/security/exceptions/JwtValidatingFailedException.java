package com.projectflow.projectflow.global.security.exceptions;

import com.projectflow.projectflow.global.exception.ErrorCode;
import com.projectflow.projectflow.global.exception.GlobalException;

public class JwtValidatingFailedException extends GlobalException {
    public static final GlobalException EXCEPTION = new JwtValidatingFailedException();

    private JwtValidatingFailedException() {
        super(ErrorCode.JWT_VALIDATE_FAILED);
    }
}
