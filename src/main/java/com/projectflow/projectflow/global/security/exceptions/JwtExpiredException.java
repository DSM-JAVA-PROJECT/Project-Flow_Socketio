package com.projectflow.projectflow.global.security.exceptions;

import com.projectflow.projectflow.global.exception.ErrorCode;
import com.projectflow.projectflow.global.exception.GlobalException;

public class JwtExpiredException extends GlobalException {
    public static final GlobalException EXCEPTION = new JwtExpiredException();

    private JwtExpiredException() {
        super(ErrorCode.JWT_VALIDATE_FAILED);
    }
}
