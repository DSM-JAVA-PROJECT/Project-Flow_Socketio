package com.projectflow.projectflow.domain.chatroom.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateChatRoomRequest {

    private List<String> emails;

}
