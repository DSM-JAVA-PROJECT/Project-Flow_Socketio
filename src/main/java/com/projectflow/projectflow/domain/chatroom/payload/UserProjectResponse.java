package com.projectflow.projectflow.domain.chatroom.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProjectResponse {

    private String projectImage;

    private String projectName;

    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private LocalDate projectStartDate;

    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private LocalDate projectEndDate;

}
