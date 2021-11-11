package com.projectflow.projectflow.domain.plan.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlanRequest {

    @NotBlank
    private String chatRoomId;

    @NotBlank
    private String planName;

    @NotBlank
    private String planEndDate;

    @NotBlank
    private String planStartDate;

    private boolean isForced;

}
