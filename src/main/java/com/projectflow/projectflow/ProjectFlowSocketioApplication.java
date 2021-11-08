package com.projectflow.projectflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class ProjectFlowSocketioApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectFlowSocketioApplication.class, args);
    }

}
