package com.goal.graph.resolver;

import com.goal.graph.model.GraphResponse;
import com.goal.graph.model.request.PageBodyIn;
import com.goal.graph.model.request.goal.SearchGoalFilter;
import com.goal.graph.model.response.goal.GoalDTO;
import com.goal.service.GoalService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GoalResolver implements GraphQLQueryResolver {
    private final GoalService goalService;

    public GraphResponse<GoalDTO> getParentGoal(SearchGoalFilter filter, PageBodyIn page) {
        return goalService.searchAllGoal(filter, page);
    }
}
