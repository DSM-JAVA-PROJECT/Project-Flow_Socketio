package com.projectflow.projectflow;

import com.projectflow.projectflow.domain.chat.entity.ChatRepository;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoomRepository;
import com.projectflow.projectflow.domain.plan.entity.CustomPlanRepository;
import com.projectflow.projectflow.domain.plan.entity.CustomPlanRepositoryImpl;
import com.projectflow.projectflow.domain.project.entity.Project;
import com.projectflow.projectflow.domain.project.entity.ProjectRepository;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.domain.user.entity.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.List;

public abstract class BasicTest {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected ChatRoomRepository chatRoomRepository;

    protected CustomPlanRepository planRepository;

    @Autowired
    protected ProjectRepository projectRepository;

    @Autowired
    protected ChatRepository chatRepository;
    protected User user;
    protected User user2;
    protected Project project;
    protected ChatRoom chatRoom;
    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        this.planRepository = new CustomPlanRepositoryImpl(mongoTemplate);
        user = userRepository.save(
                User.builder()
                        .email("email")
                        .name("name")
                        .phoneNumber("phoneNumber")
                        .profileImage("sadf")
                        .password("pwd")
                        .build()
        );
        user2 = userRepository.save(
                User.builder()
                        .email("email2")
                        .name("name")
                        .phoneNumber("phoneNumber")
                        .profileImage("sadf")
                        .password("pwd")
                        .build()
        );
        project = Project.builder()
                .endDate(LocalDate.now().plusDays(10))
                .startDate(LocalDate.now())
                .pm(user)
                .explanation("sdf")
                .logoImage("dsf")
                .title("title")
                .build();

        chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .userIds(List.of(user))
                .name("name")
                .build());
        project.getChatRooms().add(chatRoom);
        projectRepository.save(project);
    }

    @AfterEach
    void cleanUp() {
        mongoTemplate.remove(new Query(), Project.class);
        mongoTemplate.remove(new Query(), ChatRoom.class);
        mongoTemplate.remove(new Query(), User.class);
    }
}
