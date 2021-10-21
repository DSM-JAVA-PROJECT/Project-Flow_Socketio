package com.projectflow.projectflow.domain.chat.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ChatMessage {

    private String id;

    private String message;

    private String senderName;

    private String senderImage;

    private boolean isMine;

    private List<String> readerList;

    private LocalDateTime cratedAt;

}
