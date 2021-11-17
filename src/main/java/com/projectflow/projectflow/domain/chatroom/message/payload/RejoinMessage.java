package com.projectflow.projectflow.domain.chatroom.message.payload;

import com.projectflow.projectflow.global.websocket.enums.MessageType;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RejoinMessage {

    @Builder.Default
    private MessageType type = MessageType.REJOIN_CHATROOM;

    private String userEmail;

}
