package com.projectflow.projectflow.domain.plan.message;

import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.plan.entity.Plan;

public interface PlanSocketService {
    void sendCreatePlanMessage(String chatRoomId, Plan plan, SocketIOServer server);
}
