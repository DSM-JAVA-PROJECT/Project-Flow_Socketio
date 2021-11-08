package com.projectflow.projectflow.domain.chatroom.exceptions;

import com.projectflow.projectflow.global.exception.ErrorCode;
import com.projectflow.projectflow.global.exception.GlobalException;

public class ChatRoomNotFoundException extends GlobalException {
    public static final GlobalException EXCEPTION = new ChatRoomNotFoundException();

    private ChatRoomNotFoundException() {
        super(ErrorCode.CHATROOM_NOT_FOUND);
    }
}
