package com.projectflow.projectflow.domain.project.entity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Project, ObjectId> {
}
