package com.projectflow.projectflow.domain.plan.message.payload;

import com.projectflow.projectflow.global.websocket.enums.MessageType;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CreatePlanMessage {

    @Builder.Default
    private MessageType type = MessageType.PLAN;

    private String planId;

    private String planName;

    private String startDate;

    private String endDate;

    private String createdAt;

}
