package com.projectflow.projectflow.domain.plan.entity;

import com.projectflow.projectflow.BasicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class CustomPlanRepositoryImplTest extends BasicTest {

    @Test
    void 저장_성공_테스트() {
        String expectedPlanName = "name";

        Plan plan = planRepository.savePlan(chatRoom.getId().toString(), Plan.builder()
                .name(expectedPlanName)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build());
        planRepository.savePlan(chatRoom.getId().toString(), Plan.builder()
                .name("name2")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build());

        assertThat(chatRoomRepository.findById(chatRoom.getId()).get().getPlans())
                .isNotEmpty();

        assertThat(plan.getName()).isEqualTo(expectedPlanName);
    }
}