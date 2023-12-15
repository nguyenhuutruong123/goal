package com.goal.elasticsearch.service;

import com.goal.graph.model.request.goal.PageBody;
import com.goal.graph.model.request.goal.SearchGoalFilter;
import com.goal.graph.model.response.goal.GoalResponseDTO;

import java.io.IOException;
import java.util.List;

public interface ElasticSearchGoalService {
    List<GoalResponseDTO> getAllGoals(SearchGoalFilter filter, PageBody page) throws IOException;

    boolean createGoal(Object object);

    boolean createGoalValue(Object object);

    boolean createGoalSituation(Object object);

    boolean createGoalBehavior(Object object);

    boolean saveGoal(Object object, String type);
}
