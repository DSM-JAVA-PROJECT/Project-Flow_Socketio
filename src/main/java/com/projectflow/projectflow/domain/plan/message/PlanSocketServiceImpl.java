package com.projectflow.projectflow.domain.plan.message;

import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.plan.entity.Plan;
import com.projectflow.projectflow.domain.plan.message.payload.CreatePlanMessage;
import com.projectflow.projectflow.global.websocket.SocketProperty;
import org.springframework.stereotype.Service;

@Service
public class PlanSocketServiceImpl implements PlanSocketService {

    @Override
    public void sendCreatePlanMessage(String chatRoomId, Plan plan, SocketIOServer server) {
        var message = buildResponse(plan);
        server.getRoomOperations(chatRoomId)
                .sendEvent(SocketProperty.CREATE_PLAN_KEY, message);
    }

    private CreatePlanMessage buildResponse(Plan plan) {
        return CreatePlanMessage.builder()
                .createdAt(plan.getCreatedAt().toString())
                .planId(plan.getId().toString())
                .planName(plan.getName())
                .endDate(plan.getEndDate().toString())
                .startDate(plan.getStartDate().toString())
                .build();
    }

}
