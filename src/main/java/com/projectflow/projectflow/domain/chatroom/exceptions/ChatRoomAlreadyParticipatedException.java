package com.projectflow.projectflow.domain.chatroom.exceptions;

import com.projectflow.projectflow.global.exception.ErrorCode;
import com.projectflow.projectflow.global.exception.GlobalException;

public class ChatRoomAlreadyParticipatedException extends GlobalException {
    public static final GlobalException EXCEPTION = new ChatRoomAlreadyParticipatedException();
    private ChatRoomAlreadyParticipatedException() {
        super(ErrorCode.ALREADY_PARTICIPATE);
    }
}
