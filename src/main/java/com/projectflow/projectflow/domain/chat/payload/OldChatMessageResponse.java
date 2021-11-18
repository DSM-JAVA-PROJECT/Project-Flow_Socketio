package com.projectflow.projectflow.domain.chat.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OldChatMessageResponse {

    private MessageType type;

    private String id;

    private String message;

    private String senderName;

    private String senderImage;

    private boolean isMine;

    private List<String> readerList;

    private LocalDateTime createdAt;

    private String planName;

    private String startDate;

    private String endDate;

    private String planId;

    private Integer readerCount;

}
