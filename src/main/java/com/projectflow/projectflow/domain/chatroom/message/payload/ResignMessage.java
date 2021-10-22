package com.projectflow.projectflow.domain.chatroom.message.payload;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ResignMessage {

    private String userId;

    private String userEmail;

    private String userName;

    private String profileImage;

}
