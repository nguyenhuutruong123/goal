package com.goal.web.rest.controller;

import com.goal.elasticsearch.service.ElasticSearchGoalService;
import com.goal.entity.dto.GoalBehaviorDTO;
import com.goal.entity.dto.GoalDTO;
import com.goal.entity.dto.GoalSituationDTO;
import com.goal.entity.dto.GoalValueDTO;
import com.goal.web.rest.payload.RestResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.goal.common.constants.ElasticSearchConstants.GOAL_BEHAVIOR_INDEX_NAME;
import static com.goal.common.constants.ElasticSearchConstants.GOAL_SITUATION_INDEX_NAME;
import static com.goal.common.constants.ElasticSearchConstants.GOAL_VALUE_INDEX_NAME;
import static com.goal.common.constants.ElasticSearchConstants.PARENT_GOAL;


@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PutDataController {
    private final ElasticSearchGoalService elasticSearchGoalService;

    @PostMapping("/add-data/goal")
    public ResponseEntity<RestResponse<Object>> saveGoal(@RequestBody List<GoalDTO> request) {
        request.forEach(item-> elasticSearchGoalService.saveGoal(item, PARENT_GOAL));
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.builder().body(true).build());
    }

    @PostMapping("/add-data/goal-value")
    public ResponseEntity<RestResponse<Object>> saveGoalValue(@RequestBody List<GoalValueDTO> request) {
        request.forEach(item-> elasticSearchGoalService.saveGoal(item, GOAL_VALUE_INDEX_NAME));
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.builder().body(true).build());
    }

    @PostMapping("/add-data/goal-situation")
    public ResponseEntity<RestResponse<Object>> saveGoalSituation(@RequestBody List<GoalSituationDTO> request) {
        request.forEach(item-> elasticSearchGoalService.saveGoal(item, GOAL_SITUATION_INDEX_NAME));
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.builder().body(true).build());
    }

    @PostMapping("/add-data/goal-behavior")
    public ResponseEntity<RestResponse<Object>> saveGoalBehavior(@RequestBody List<GoalBehaviorDTO> request) {
        request.forEach(item-> elasticSearchGoalService.saveGoal(item, GOAL_BEHAVIOR_INDEX_NAME));
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.builder().body(true).build());
    }
}
