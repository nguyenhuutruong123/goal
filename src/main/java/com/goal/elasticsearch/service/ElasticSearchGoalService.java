package com.goal.elasticsearch.service;

import com.goal.graph.model.response.goal.GoalResponseDTO;

import java.io.IOException;
import java.util.List;

public interface ElasticSearchGoalService {
    List<GoalResponseDTO> getAllGoals(Integer page, Integer size) throws IOException;
}
