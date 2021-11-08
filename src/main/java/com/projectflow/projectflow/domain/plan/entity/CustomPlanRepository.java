package com.projectflow.projectflow.domain.plan.entity;

public interface CustomPlanRepository {
    Plan savePlan(String chatRoomId, Plan plan);
}
