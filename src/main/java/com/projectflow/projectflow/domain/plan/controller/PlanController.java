package com.projectflow.projectflow.domain.plan.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.domain.plan.entity.Plan;
import com.projectflow.projectflow.domain.plan.message.PlanSocketService;
import com.projectflow.projectflow.domain.plan.payload.CreatePlanRequest;
import com.projectflow.projectflow.domain.plan.payload.JoinPlanRequest;
import com.projectflow.projectflow.domain.plan.payload.ResignPlanRequest;
import com.projectflow.projectflow.domain.plan.service.PlanService;
import com.projectflow.projectflow.domain.user.entity.User;
import com.projectflow.projectflow.domain.user.entity.facade.UserFacade;
import com.projectflow.projectflow.global.websocket.annotations.SocketController;
import com.projectflow.projectflow.global.websocket.annotations.SocketMapping;
import com.projectflow.projectflow.global.websocket.security.SocketAuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@SocketController
public class PlanController {

    private final PlanService planService;
    private final PlanSocketService planSocketService;
    private final SocketAuthenticationFacade authenticationFacade;
    private final UserFacade userFacade;

    @SocketMapping(endpoint = "plan.create", requestCls = CreatePlanRequest.class)
    public void createPlan(SocketIOClient client, SocketIOServer server, CreatePlanRequest request) {
        User user = authenticationFacade.getCurrentUser(client);
        Plan plan = planService.createPlan(request, user);
        planSocketService.sendCreatePlanMessage(request.getChatRoomId(), plan, user, server, request.getIsForced());
    }

    @SocketMapping(endpoint = "plan.join", requestCls = JoinPlanRequest.class)
    public void joinPlan(SocketIOClient client, SocketIOServer server, JoinPlanRequest request) {
        User user = authenticationFacade.getCurrentUser(client);
        Plan plan = planService.joinPlan(request, user);
        planSocketService.sendJoinPlanMessage(request.getChatRoomId(), plan, user, server);
    }

    @SocketMapping(endpoint = "plan.resign", requestCls = ResignPlanRequest.class)
    public void resignPlan(SocketIOClient client, SocketIOServer server, ResignPlanRequest request) {
        User user = authenticationFacade.getCurrentUser(client);
        Plan plan = planService.resignPlan(request, user);
        planSocketService.sendResignPlanMessage(request.getChatRoomId(), plan, user, server);
    }

    @DeleteMapping("/plan/{planId}")
    public void removePlan(@PathVariable String planId) {
        planService.removePlan(planId);
    }

    @DeleteMapping("/plan/resign/{chatRoomId}/{planId}")
    public void resignPlan(@PathVariable String planId,
                           @PathVariable String chatRoomId,
                           SocketIOServer server) {
        User user = userFacade.getCurrentUser();
        Plan plan = planService.resignPlan(new ResignPlanRequest(planId, chatRoomId), user);
        planSocketService.sendResignPlanMessage(chatRoomId, plan, user, server);
    }
}
