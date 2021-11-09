package com.projectflow.projectflow.domain.plan.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.plan.entity.Plan;
import com.projectflow.projectflow.domain.plan.message.PlanSocketService;
import com.projectflow.projectflow.domain.plan.payload.CreatePlanRequest;
import com.projectflow.projectflow.domain.plan.payload.JoinPlanRequest;
import com.projectflow.projectflow.domain.plan.service.PlanService;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.global.websocket.annotations.SocketController;
import com.projectflow.projectflow.global.websocket.annotations.SocketMapping;
import com.projectflow.projectflow.global.websocket.security.SocketAuthenticationFacade;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SocketController
public class PlanController {

    private final PlanService planService;
    private final PlanSocketService planSocketService;
    private final SocketAuthenticationFacade authenticationFacade;

    @SocketMapping(endpoint = "plan.create", requestCls = CreatePlanRequest.class)
    public void createPlan(SocketIOClient client, SocketIOServer server, CreatePlanRequest request) {
        User user = authenticationFacade.getCurrentUser(client);
        Plan plan = planService.createPlan(request, user);
        planSocketService.sendCreatePlanMessage(request.getChatRoomId(), plan, server);
    }

    @SocketMapping(endpoint = "plan.join", requestCls = JoinPlanRequest.class)
    public void joinPlan(SocketIOClient client, SocketIOServer server, JoinPlanRequest request) {
        User user = authenticationFacade.getCurrentUser(client);
        Plan plan = planService.joinPlan(request, user);
        planSocketService.sendJoinPlanMessage(request.getChatRoomId(), plan, user, server);
    }
}
