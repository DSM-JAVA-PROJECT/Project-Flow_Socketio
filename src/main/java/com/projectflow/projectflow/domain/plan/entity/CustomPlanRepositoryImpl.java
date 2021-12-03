package com.projectflow.projectflow.domain.plan.entity;

import com.mongodb.client.result.UpdateResult;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import com.projectflow.projectflow.domain.chatroom.entity.ChatRoomRepository;
import com.projectflow.projectflow.domain.chatroom.exceptions.ChatRoomNotFoundException;
import com.projectflow.projectflow.domain.plan.exceptions.PlanNotFoundException;
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
    private final ChatRoomRepository chatRoomRepository;

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
    public Plan joinPlan(String planId, User user) {
        UpdateResult result = mongoTemplate.updateFirst(query(where("plans.id").is(planId)),
                new Update().push("plans.$[].planUsers", new PlanUser(user)),
                ChatRoom.class);

        if (result.getMatchedCount() != 1) {
            throw ChatRoomNotFoundException.EXCEPTION;
        }

        List<Plan> plans = mongoTemplate.findOne(query(where("plans.id").is(planId)),
                ChatRoom.class).getPlans();

        return plans.stream()
                .filter(plan -> plan.getId().toString().equals(planId))
                .findFirst()
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
    }

    @Override
    public Plan findById(String planId) {
        ChatRoom chatRoom = mongoTemplate.findOne(query(where("plans.id").is(planId)),
                ChatRoom.class);

        if (chatRoom == null) {
            throw ChatRoomNotFoundException.EXCEPTION;
        }

        return chatRoom.getPlans().stream().filter(plan -> plan.getId().toString().equals(planId))
                .findFirst()
                .orElseThrow(() -> PlanNotFoundException.EXCEPTION);
    }

    @Override
    public void removePlan(String planId) {
        ChatRoom chatRoom = mongoTemplate.findOne(query(where("plans.id").is(planId)),
                ChatRoom.class);

        if (chatRoom == null) {
            throw ChatRoomNotFoundException.EXCEPTION;
        }

        chatRoom.getPlans().removeIf(plan -> plan.getId().toString().equals(planId));

        chatRoomRepository.save(chatRoom);
    }
}
