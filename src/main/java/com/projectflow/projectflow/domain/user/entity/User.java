package com.projectflow.projectflow.domain.user.entity;

import com.projectflow.projectflow.domain.project.entity.Project;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "email")
@Document(collection = "user")
public class User {

    @MongoId
    private ObjectId id;

    @Indexed(unique = true)
    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    private String profileImage;

    @NotBlank
    private String phoneNumber;

    @DBRef(lazy = true)
    private List<Project> projects;

    @Builder
    private User(String email, String name, String password, String profileImage, String phoneNumber) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.profileImage = profileImage;
        this.phoneNumber = phoneNumber;
        this.projects = new ArrayList<>();
    }

}
