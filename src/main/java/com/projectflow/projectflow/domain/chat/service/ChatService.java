package com.projectflow.projectflow.domain.chat.service;

import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.chat.payload.ChatPinRequest;
import com.projectflow.projectflow.domain.chat.payload.ChatRequest;
import com.projectflow.projectflow.domain.chat.payload.ImageChatRequest;
import com.projectflow.projectflow.domain.chat.payload.OldChatMessageListResponse;
import com.projectflow.projectflow.domain.user.entity.User;
import org.springframework.data.domain.Pageable;

public interface ChatService {
    Chat saveMessage(ChatRequest request, User user);

    Chat saveImageMessage(ImageChatRequest request, User user);

    String removeMessage(String chatId);

    OldChatMessageListResponse getOldChatMessage(String chatRoomId, Pageable pageable);

    void pinMessage(ChatPinRequest request, User user);

}
