package com.projectflow.projectflow.domain.chatroom.entity;

import com.projectflow.projectflow.domain.plan.entity.Plan;
import com.projectflow.projectflow.domain.user.entity.User;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chatroom")
public class ChatRoom {

    @MongoId
    private ObjectId id;

    @NotBlank
    private String name;

    @DBRef
    private List<User> userIds;

    private List<Plan> plans;

    private String profileImage;

    @Builder
    private ChatRoom(String name, List<User> userIds, String profileImage) {
        this.name = name;
        this.userIds = userIds;
        this.plans = new ArrayList<>();
        this.profileImage = profileImage;
    }

    public void updateChatRoomImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void updateChatRoomName(String name) {
        this.name = name;
    }
}
