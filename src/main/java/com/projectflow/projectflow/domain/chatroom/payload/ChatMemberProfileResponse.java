package com.projectflow.projectflow.domain.chatroom.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMemberProfileResponse {

    private String userEmail;

    private String userName;

    private String phoneNumber;

    private String profileImage;

    private List<UserProjectResponse> projectResponses;
}
