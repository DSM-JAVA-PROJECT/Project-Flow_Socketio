package com.projectflow.projectflow.domain.chatroom.message.payload;

import com.projectflow.projectflow.global.websocket.enums.MessageType;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class JoinMessage {

    @Builder.Default
    private MessageType type = MessageType.JOIN_CHATROOM;

    private String userId;

    private String userEmail;

    private String userName;

    private String profileImage;

}
