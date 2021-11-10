package com.projectflow.projectflow.domain.plan.message;

import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.plan.entity.Plan;
import com.projectflow.projectflow.domain.user.entity.User;

public interface PlanSocketService {
    void sendCreatePlanMessage(String chatRoomId, Plan plan, SocketIOServer server);

    void sendJoinPlanMessage(String chatRoomId, Plan plan, User sender, SocketIOServer server);

    void sendResignPlanMessage(String chatRoomId, Plan plan, User sender, SocketIOServer server);
}
