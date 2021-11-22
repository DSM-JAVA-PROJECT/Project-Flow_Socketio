package com.projectflow.projectflow.domain.chat.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageChatRequest {

    private String imageUrl;

    private String chatRoomId;

}
