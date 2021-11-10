package com.projectflow.projectflow.domain.chat.payload;

import com.projectflow.projectflow.global.websocket.enums.MessageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class OldChatMessageResponse {

    @Builder.Default
    private MessageType type = MessageType.MESSAGE;

    private String id;

    private String message;

    private String senderName;

    private String senderImage;

    private boolean isMine;

    private List<String> readerList;

    private LocalDateTime createdAt;


}
