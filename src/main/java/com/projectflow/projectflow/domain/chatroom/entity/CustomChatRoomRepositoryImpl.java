package com.projectflow.projectflow.domain.chatroom.entity;

import com.mongodb.DBRef;
import com.projectflow.projectflow.domain.project.entity.Project;
import com.projectflow.projectflow.domain.project.entity.ProjectRepository;
import com.projectflow.projectflow.domain.project.exceptions.ProjectNotFoundException;
import com.projectflow.projectflow.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.AggregationUpdate.update;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
public class CustomChatRoomRepositoryImpl implements CustomChatRoomRepository {

    private final MongoTemplate mongoTemplate;
    private final ProjectRepository projectRepository;


    // TODO: 2021-09-17 프로젝트 레포지토리로 이동하렴
    @Override
    public boolean isNotProjectMember(User user, String projectId) {
        return !mongoTemplate.exists(query(where("_id").is(projectId)
                        .andOperator(where("projectUsers.userId").is(user.getId()))),
                Project.class);
    }

    @Override
    public void joinChatRoom(String chatRoomId, User user) {
        mongoTemplate.findAndModify(query(where("_id").is(chatRoomId)),
                new Update().push("userIds", user),
                ChatRoom.class);
    }

    @Override
    public boolean isChatRoomMember(String chatRoomId, User user) {
        return mongoTemplate.findOne(query(where("_id").is(chatRoomId)
                        .andOperator(where("userIds.$id").is(user.getId()))),   // userIds 의 id 요소가 user.getId인 요소 find
                ChatRoom.class) != null;
    }

    @Override
    public void deleteMember(String chatRoomId, User user) {
        mongoTemplate.findAndModify(query(where("_id").is(chatRoomId)
                        .andOperator(where("userIds.$id").is(user.getId()))),
                new Update().pull("userIds", user),
                ChatRoom.class);
    }

    @Override
    public List<ChatRoom> findChatRoomList(String projectId, User user) {
        Project project = projectRepository.findById(new ObjectId(projectId))
                .orElseThrow(() -> ProjectNotFoundException.EXCEPTION);

        return project.getChatRooms().stream().filter(chatRoom -> chatRoom.getUserIds().stream().anyMatch(user::equals))
                .collect(Collectors.toList());
    }

    @Override
    public void setPinChat(String chatRoomId, String chatId) {

        mongoTemplate.findAndModify(query(where("_id").is(chatRoomId)),
                update().set("pinnedChat")
                        .toValueOf(new DBRef("chat", new ObjectId(chatId))),
                ChatRoom.class);
    }

}
