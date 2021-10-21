package com.projectflow.projectflow.domain.chat.service;

import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.chat.payload.ChatRequest;
import com.projectflow.projectflow.domain.chat.payload.OldChatMessageListResponse;
import org.springframework.data.domain.Pageable;

public interface ChatService {
    Chat saveMessage(String chatRoomId, ChatRequest request);

    String removeMessage(String chatId);

    OldChatMessageListResponse getOldChatMessage(String chatRoomId, Pageable pageable);
}
