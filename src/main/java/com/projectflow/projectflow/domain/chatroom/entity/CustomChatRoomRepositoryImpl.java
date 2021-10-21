package com.projectflow.projectflow.domain.chatroom.entity;

import com.projectflow.projectflow.domain.project.entity.Project;
import com.projectflow.projectflow.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
public class CustomChatRoomRepositoryImpl implements CustomChatRoomRepository {

    private final MongoTemplate mongoTemplate;


    // TODO: 2021-09-17 프로젝트 레포지토리로 이동하렴
    @Override
    public boolean isProjectMember(User user, String projectId) {
        return mongoTemplate.exists(query(where("id").is(projectId)
                .andOperator(where("projectUsers").elemMatch(where("id").is(user.getId())))),
                Project.class);
    }

    @Override
    public String joinChatRoom(String chatRoomId, User user) {
        return mongoTemplate.findAndModify(query(where("_id").is(chatRoomId)),
                new Update().push("userIds", user),
                ChatRoom.class).getId().toString();
    }

    @Override
    public boolean isChatRoomMember(String chatRoomId, User user) {
        return mongoTemplate.findOne(query(where("_id").is(chatRoomId)
                        .andOperator(where("userIds.$id").is(user.getId()))),   // userIds 의 id 요소가 user.getId인 요소 find
                ChatRoom.class) != null;
    }

    @Override
    public void deleteMember(String chatRoomId, User user) {
        mongoTemplate.remove(query(where("_id").is(chatRoomId)
                .andOperator(where("userIds.$id").is(user.getId()))),
                ChatRoom.class);
    }

}
