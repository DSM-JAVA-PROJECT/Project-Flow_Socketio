package com.projectflow.projectflow.domain.chatroom.message.payload;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RejoinMessage {

    private String userEmail;

}
