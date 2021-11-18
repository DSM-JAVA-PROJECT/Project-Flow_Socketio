package com.projectflow.projectflow.domain.chat.message.payload;

import com.projectflow.projectflow.global.websocket.enums.MessageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ChatMessage {

    @Builder.Default
    private MessageType type = MessageType.MESSAGE;

    private String id;

    private String message;

    private String senderName;

    private String senderImage;

    private List<String> readerList;

    private Integer readerCount;

    private boolean isMine;

    private String createdAt;

}
