package com.projectflow.projectflow.domain.chatroom.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomResponse {

    private String id;

    private String chatRoomName;

    private String chatRoomImage;

}
