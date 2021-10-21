package com.projectflow.projectflow.global.auth.exceptions;

import com.projectflow.projectflow.global.exception.ErrorCode;
import com.projectflow.projectflow.global.exception.GlobalException;

public class UserNotFoundException extends GlobalException {
    public static final GlobalException EXCEPTION = new UserNotFoundException();

    private UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
