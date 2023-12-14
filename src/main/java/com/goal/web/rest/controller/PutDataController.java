package com.goal.web.rest.controller;

import com.goal.constants.GlobalConstant;
import com.goal.entity.dto.*;
import com.goal.service.GoalServiceImpl;
import com.goal.service.SearchService;
import com.goal.service.impl.ParentServiceImpl;
import com.goal.web.rest.payload.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import static com.goal.constants.GlobalConstant.*;


@RestController
@RequestMapping("/api")
public class PutDataController {
    @Autowired
    private SearchService service;

    @Autowired
    private ParentServiceImpl parentServiceImpl;

    @PostMapping("/add-data/goal")
    public ResponseEntity<RestResponse<Object>> saveGoal(@RequestBody List<GoalDTO> request) throws IOException {
        request.forEach(item->{
            try {
                parentServiceImpl.saveGoal(item,PARENT_GOAL);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.builder().body(true).build());
    }
    @PostMapping("/add-data/goal-value")
    public ResponseEntity<RestResponse<Object>> saveGoalValue(@RequestBody List<GoalValueDTO> request) throws IOException {
        request.forEach(item->{
            try {
                parentServiceImpl.saveGoal(item, CHILD_GOAL_VALUE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.builder().body(true).build());
    }
    @PostMapping("/add-data/goal-situation")
    public ResponseEntity<RestResponse<Object>> saveGoalSituation(@RequestBody List<GoalSituationDTO> request) throws IOException {
        request.forEach(item->{
            try {
                parentServiceImpl.saveGoal(item, CHILD_GOAL_SITUATION);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.builder().body(true).build());
    }
    @PostMapping("/add-data/goal-behavior")
    public ResponseEntity<RestResponse<Object>> saveGoalBehavior(@RequestBody List<GoalBehaviorDTO> request) throws IOException {
        request.forEach(item->{
            try {
                parentServiceImpl.saveGoal(item, CHILD_GOAL_BEHAVIOR);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.builder().body(true).build());
    }
}
