package com.projectflow.projectflow.domain.plan.message.payload;

import com.projectflow.projectflow.global.websocket.enums.MessageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class JoinPlanMessage {

    @Builder.Default
    private MessageType type = MessageType.JOIN_PLAN;

    private String planId;

    private String planName;

    private String startDate;

    private String endDate;

    private String createdAt;

    private boolean isMine;

    private String senderName;

    private String senderImage;

}
