package com.projectflow.projectflow.domain.plan.entity;

import com.projectflow.projectflow.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document
public class Plan {

    @MongoId
    private ObjectId id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate finishDate;

    private LocalDateTime createdAt;

    private List<PlanUser> planUsers;

    @Builder
    private Plan(String name, LocalDate endDate, LocalDate startDate, LocalDate finishDate, LocalDateTime createdAt, List<User> users) {
        this.id = new ObjectId();
        List<PlanUser> planUsers = users != null ? users.stream().map(user -> PlanUser.builder()
                        .isFinished(false)
                        .user(user)
                        .build())
                .collect(Collectors.toList()) : null;

        this.name = name;
        this.endDate = endDate;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.planUsers = planUsers;
        this.createdAt = createdAt;
    }
}
