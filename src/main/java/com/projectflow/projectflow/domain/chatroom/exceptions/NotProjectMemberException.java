package com.projectflow.projectflow.domain.chatroom.exceptions;

import com.projectflow.projectflow.global.exception.ErrorCode;
import com.projectflow.projectflow.global.exception.GlobalException;

public class NotProjectMemberException extends GlobalException {
    public static final GlobalException EXCEPTION = new NotProjectMemberException();
    private NotProjectMemberException() {
        super(ErrorCode.NOT_PROJECT_MEMBER);
    }
}
