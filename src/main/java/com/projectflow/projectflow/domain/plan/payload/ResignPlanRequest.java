package com.projectflow.projectflow.domain.plan.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResignPlanRequest {

    @NotBlank
    private String chatRoomId;

    @NotBlank
    private String planId;

}
