package com.projectflow.projectflow.domain.plan.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

    @CreatedDate
    private LocalDate createdAt;

    private List<PlanUser> planUsers;

    @Builder
    private Plan(String name, LocalDate endDate, LocalDate startDate, LocalDate finishDate) {
        this.name = name;
        this.endDate = endDate;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.planUsers = new ArrayList<>();
    }
}
