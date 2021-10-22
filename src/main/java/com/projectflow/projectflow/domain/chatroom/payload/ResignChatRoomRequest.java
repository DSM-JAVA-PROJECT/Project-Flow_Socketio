package com.projectflow.projectflow.domain.chatroom.payload;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResignChatRoomRequest {
    private String chatRoomId;
}
