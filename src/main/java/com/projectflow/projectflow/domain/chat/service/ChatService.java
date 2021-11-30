package com.projectflow.projectflow.domain.chat.service;

import com.projectflow.projectflow.domain.chat.entity.Chat;
import com.projectflow.projectflow.domain.chat.payload.*;
import com.projectflow.projectflow.domain.user.entity.User;
import org.springframework.data.domain.Pageable;

public interface ChatService {
    Chat saveMessage(ChatRequest request, User user);

    Chat saveImageMessage(ImageChatRequest request, User user);

    String removeMessage(String chatId);

    OldChatMessageListResponse getOldChatMessage(String chatRoomId, Pageable pageable);

    PinResponse getPinnedChat(String chatRoomId);

    void deletePinnedChat(String chatRoomId);

    void pinMessage(ChatPinRequest request, User user);

}
