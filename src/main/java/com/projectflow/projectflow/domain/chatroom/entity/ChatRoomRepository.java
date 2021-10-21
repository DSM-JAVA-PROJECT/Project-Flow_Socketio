package com.projectflow.projectflow.domain.chatroom.entity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends CustomChatRoomRepository, MongoRepository<ChatRoom, ObjectId> {
}
