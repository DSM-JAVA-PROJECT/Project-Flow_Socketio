package com.projectflow.projectflow.domain.chatroom.message.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinListMessage {
    private List<JoinMessage> joinMessages;
}
