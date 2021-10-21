package com.projectflow.projectflow.domain.project.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberResponse {

    private String name;

    private String email;

    private String profileImage;

}
