package com.projectflow.projectflow.global.auth.exceptions;

import com.projectflow.projectflow.global.exception.ErrorCode;
import com.projectflow.projectflow.global.exception.GlobalException;

public class AuthUserNotFoundException extends GlobalException {
    public static final GlobalException EXCEPTION = new AuthUserNotFoundException();

    private AuthUserNotFoundException() {
        super(ErrorCode.AUTH_USER_NOT_FOUND);
    }
}
