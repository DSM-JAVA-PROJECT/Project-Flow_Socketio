package com.projectflow.projectflow.domain.plan.message.payload;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CreatePlanMessage {

    private String planId;

    private String planName;

    private String startDate;

    private String endDate;

    private String createdAt;

}
