package com.projectflow.projectflow.domain.chat.entity.enums;

import com.projectflow.projectflow.global.websocket.enums.MessageType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatType {
    CHAT(MessageType.MESSAGE),
    PLAN(MessageType.PLAN);

    private final MessageType messageType;
}
