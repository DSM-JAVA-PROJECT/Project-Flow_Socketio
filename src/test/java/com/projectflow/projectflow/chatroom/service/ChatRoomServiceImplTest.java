package com.projectflow.projectflow.chatroom.service;//package com.projectflow.projectflowwebsocket.domain.chatroom.service;
//
//import com.projectflow.projectflowwebsocket.domain.chatroom.entity.ChatRoom;
//import com.projectflow.projectflowwebsocket.domain.chatroom.entity.ChatRoomRepository;
//import com.projectflow.projectflowwebsocket.domain.chatroom.payload.CreateChatRoomRequest;
//import com.projectflow.projectflowwebsocket.domain.project.entity.Project;
//import com.projectflow.projectflowwebsocket.domain.project.entity.ProjectRepository;
//import com.projectflow.projectflowwebsocket.domain.user.entity.User;
//import com.projectflow.projectflowwebsocket.global.auth.facade.AuthenticationFacade;
//import org.bson.types.ObjectId;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doReturn;
//
//@ExtendWith(MockitoExtension.class)
//class ChatRoomServiceImplTest {
//
//    @InjectMocks
//    private ChatRoomServiceImpl chatRoomService;            // 실제로 사용할 Mock 객체
//
//    @Mock
//    private ChatRoomRepository chatRoomRepository;      // Service 에 넣어줄 Mock 객체
//
//    @Mock
//    private ProjectRepository projectRepository;
//
//    @Mock
//    private AuthenticationFacade authenticationFacade;
//
//    @Test
//    void 채팅방_생성() {
//        CreateChatRoomRequest request = buildChatRoomRequest();
//        ChatRoom chatRoom = buildChatRoom();
//
//        // 가짜 Repository 에 가짜 데이터를 넣어준다.
//        given(chatRoomRepository.save(any()))
//                .willReturn(buildChatRoom());
//        given(chatRoomRepository.findAll()).willReturn(List.of(chatRoom));
//        given(authenticationFacade.getCurrentUser()).willReturn(User.builder().email("sda").build());
//        given(projectRepository.findById(new ObjectId(""))).willReturn(Optional.of(Project.builder().build()));
//
//        // when
//        chatRoomService.createChatRoom("", request);
//    }
//
//    private CreateChatRoomRequest buildChatRoomRequest() {
//        return CreateChatRoomRequest.builder()
//                .name("name")
//                .build();
//    }
//
//    private ChatRoom buildChatRoom() {
//        return ChatRoom.builder()
//                .name("name")
//                .build();
//    }
//
//}