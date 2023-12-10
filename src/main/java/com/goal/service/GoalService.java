package com.goal.service;

import com.goal.graph.model.GraphResponse;
import com.goal.graph.model.request.PageBodyIn;
import com.goal.graph.model.request.goal.SearchGoalFilter;
import com.goal.graph.model.response.goal.GoalDTO;

public interface GoalService {
    GraphResponse<GoalDTO> searchAllGoal(SearchGoalFilter filter, PageBodyIn page);
}
