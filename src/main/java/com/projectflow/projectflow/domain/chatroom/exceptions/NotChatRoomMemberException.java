package com.projectflow.projectflow.domain.chatroom.exceptions;

import com.projectflow.projectflow.global.exception.ErrorCode;
import com.projectflow.projectflow.global.exception.GlobalException;

public class NotChatRoomMemberException extends GlobalException {
    public static final GlobalException EXCEPTION = new NotChatRoomMemberException();
    private NotChatRoomMemberException() {
        super(ErrorCode.NOT_CHATROOM_MEMBER);
    }
}
