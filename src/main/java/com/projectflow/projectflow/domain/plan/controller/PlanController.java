package com.projectflow.projectflow.domain.plan.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.plan.payload.CreatePlanRequest;
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
    private final SocketAuthenticationFacade authenticationFacade;

    @SocketMapping(endpoint = "/plan/create", requestCls = CreatePlanRequest.class)
    public void createPlan(SocketIOClient client, SocketIOServer server, CreatePlanRequest request) {
        User user = authenticationFacade.getCurrentUser(client);
        planService.createPlan(request, user);
    }
}
