package com.goal.service;

import com.goal.graph.model.GraphResponse;
import com.goal.graph.model.request.PageBodyIn;
import com.goal.graph.model.request.goal.SearchGoalFilter;
import com.goal.graph.model.response.goal.GoalDTO;
import org.springframework.stereotype.Service;

@Service
public class GoalServiceImpl implements GoalService {

    @Override
    public GraphResponse<GoalDTO> searchAllGoal(SearchGoalFilter filter, PageBodyIn page) {
        return new GraphResponse<>();
    }
}
