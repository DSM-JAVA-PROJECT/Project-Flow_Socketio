package com.projectflow.projectflow.domain.plan.entity;

import com.mongodb.client.result.UpdateResult;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import com.projectflow.projectflow.domain.chatroom.exceptions.ChatRoomNotFoundException;
import com.projectflow.projectflow.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
@Service
public class CustomPlanRepositoryImpl implements CustomPlanRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Plan savePlan(String chatRoomId, Plan plan) {
        UpdateResult result = mongoTemplate.updateFirst(query(where("_id").is(chatRoomId)),
                new Update().push("plans", plan),
                ChatRoom.class);

        if (result.getMatchedCount() != 1) {
            throw ChatRoomNotFoundException.EXCEPTION;
        }
        List<Plan> plans = mongoTemplate.findOne(query(where("_id").is(chatRoomId)),
                ChatRoom.class).getPlans();

        return plans.get(plans.size() - 1);

    }

    @Override
    public void joinPlan(String planId, User user) {
        UpdateResult result = mongoTemplate.updateFirst(query(where("plans.$id").is(planId)),
                new Update().push("planUsers", user),
                ChatRoom.class);

        if (result.getMatchedCount() != 1) {
            throw ChatRoomNotFoundException.EXCEPTION;
        }
    }
}
