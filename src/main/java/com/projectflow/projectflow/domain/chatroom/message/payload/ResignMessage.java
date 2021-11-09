package com.projectflow.projectflow.domain.chatroom.message.payload;

import com.projectflow.projectflow.global.websocket.enums.MessageType;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ResignMessage {

    private MessageType type = MessageType.CHATROOM;

    private String userId;

    private String userEmail;

    private String userName;

    private String profileImage;

}
