//package com.projectflow.projectflow.domain.chatroom.controller;
//
//import com.projectflow.projectflow.domain.chatroom.service.ChatRoomService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RestController
//public class ChatRoomController {
//
//    private final ChatRoomService chatRoomService;
//
//    @MessageMapping("/create/chatroom/{projectId}")
//    public int createChatRoom(@DestinationVariable String projectId,
//                              @Payload Message<CreateChatRoomRequest> request) {
//        chatRoomService.createChatRoom(projectId, request.getPayload());
//        return 201;
//    }
//
//    @MessageMapping("/join/chatroom/{chatRoomId}")
//    public int joinChatRoom(@DestinationVariable String chatRoomId) {
//        String roomId = chatRoomService.joinChatRoom(chatRoomId);
//        messageService.sendJoinMessage(roomId);
//        return 200;
//    }
//    @MessageMapping("/resign/chatroom/{chatRoomId}")
//    public int resign(@DestinationVariable String chatRoomId) {
//        chatRoomService.resignChatRoom(chatRoomId);
//        messageService.sendResignMessage(chatRoomId);
//        return 200;
//    }
//
//}
