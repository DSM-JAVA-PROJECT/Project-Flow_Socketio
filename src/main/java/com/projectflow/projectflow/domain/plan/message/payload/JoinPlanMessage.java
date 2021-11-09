package com.projectflow.projectflow.domain.plan.message.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class JoinPlanMessage {

    private String planId;

    private String planName;

    private String startDate;

    private String endDate;

    private String createdAt;

    private boolean isMine;

}
