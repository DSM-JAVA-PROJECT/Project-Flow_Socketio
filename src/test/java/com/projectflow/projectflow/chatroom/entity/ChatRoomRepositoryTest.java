package com.projectflow.projectflow.chatroom.entity;

import com.projectflow.projectflow.BasicTest;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class ChatRoomRepositoryTest extends BasicTest {

    @Test
    void 저장_성공_테스트() {
        ChatRoom chatRoom1 = chatRoomRepository.save(ChatRoom.builder()
                .userIds(List.of(user))
                .name("name")
                .build());
        assertThat(chatRoomRepository.findById(chatRoom1.getId())).isPresent();
    }

    @Test
    void 미참가_여부_테스트() {
        assertThat(chatRoomRepository.isChatRoomMember(chatRoom.getId().toString(), user2)).isFalse();
    }

    @Test
    void 참가_테스트() {
        chatRoomRepository.joinChatRoom(chatRoom.getId().toString(), user2);
        assertThat(chatRoomRepository.isChatRoomMember(chatRoom.getId().toString(), user2)).isTrue();
    }

    @Test
    void 탈퇴_테스트() {
        chatRoomRepository.deleteMember(chatRoom.getId().toString(), user2);
        assertThat(chatRoomRepository.isChatRoomMember(chatRoom.getId().toString(), user2)).isFalse();
    }

}