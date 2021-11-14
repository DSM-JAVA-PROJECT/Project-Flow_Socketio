package com.projectflow.projectflow.domain.chat.entity;

import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.websocket.enums.MessageType;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chat")
public class Chat {

    @MongoId
    private ObjectId id;

    @NotBlank
    private String message;

    @DBRef
    private User sender;

    @DBRef(lazy = true)
    private ChatRoom chatRoom;

    @DBRef(lazy = true)
    private List<User> receiver;

    private String planId;

    @CreatedDate
    private LocalDateTime createdAt;

    private MessageType messageType;

    @Builder
    private Chat(String message, User sender, ChatRoom chatRoom, List<User> receiver, String planId, MessageType messageType) {
        this.message = message;
        this.sender = sender;
        this.chatRoom = chatRoom;
        this.receiver = receiver;
        this.messageType = messageType;
        this.planId = planId;
    }
}
