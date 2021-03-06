package com.projectflow.projectflow.domain.plan.message.payload;

import com.projectflow.projectflow.global.websocket.enums.MessageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ResignPlanMessage {

    @Builder.Default
    private MessageType type = MessageType.RESIGN_PLAN;

    private String planId;

    private String planName;

    private String startDate;

    private String endDate;

    private String createdAt;

    private boolean isMine;

    private String senderName;

    private String senderImage;

    private List<String> readerList;

}
