package com.projectflow.projectflow.domain.plan.service;

import com.projectflow.projectflow.domain.plan.entity.Plan;
import com.projectflow.projectflow.domain.plan.payload.CreatePlanRequest;
import com.projectflow.projectflow.domain.user.entity.User;

public interface PlanService {
    Plan createPlan(CreatePlanRequest request, User user);
}
