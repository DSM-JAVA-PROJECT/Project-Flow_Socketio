package com.projectflow.projectflow.domain.chat.exceptions;

import com.projectflow.projectflow.global.exception.ErrorCode;
import com.projectflow.projectflow.global.exception.GlobalException;

public class UserNotMessageOwnerException extends GlobalException {
    public static final GlobalException EXCEPTION = new UserNotMessageOwnerException();

    private UserNotMessageOwnerException() {
        super(ErrorCode.NOT_MESSAGE_OWNER);
    }
}
