package com.projectflow.projectflow.domain.plan.entity;

import com.projectflow.projectflow.domain.user.entity.User;

public interface CustomPlanRepository {
    Plan savePlan(String chatRoomId, Plan plan);

    Plan joinPlan(String planId, User user);

    Plan findById(String planId);

    void removePlan(String planId);
}
