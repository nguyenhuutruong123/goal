package com.goal.graph.resolver;

import com.goal.elasticsearch.service.ElasticSearchGoalService;
import com.goal.graph.model.response.goal.GoalResponseDTO;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class GoalResolver implements GraphQLQueryResolver {
    private final ElasticSearchGoalService elasticSearchGoalService;

    public List<GoalResponseDTO> getAllGoals(Integer page, Integer size) throws IOException {
        return elasticSearchGoalService.getAllGoals(page, size);
    }
}
