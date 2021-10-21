package com.projectflow.projectflow.domain.project.entity;

import com.projectflow.projectflow.domain.chatroom.entity.ChatRoom;
import com.projectflow.projectflow.domain.plan.entity.Plan;
import com.projectflow.projectflow.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "project")
public class Project {

    @MongoId
    private ObjectId id;

    @NotBlank
    private String projectName;

    @NotBlank
    private String title;

    private String explanation;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotBlank
    private String logoImage;

    @DBRef(lazy = true)
    private List<ChatRoom> chatRooms = new ArrayList<>();

    private List<ProjectUser> projectUsers = new ArrayList<>();

    private List<Plan> plans;

    private boolean isFinished;

    @NotBlank
    @DBRef(lazy = true)
    private User pm;

    @Builder
    private Project(String projectName, String title, String explanation, LocalDate startDate, LocalDate endDate, String logoImage, User pm) {
        this.projectName = projectName;
        this.title = title;
        this.explanation = explanation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.logoImage = logoImage;
        this.chatRooms = new ArrayList<>();
        this.projectUsers = new ArrayList<>();
        this.isFinished = false;
        this.pm = pm;
    }
}
