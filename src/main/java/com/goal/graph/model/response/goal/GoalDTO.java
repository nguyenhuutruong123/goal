package com.goal.graph.model.response.goal;

import lombok.Data;

import java.util.List;

@Data
public class GoalDTO {
    private Long id;
    private String name;
    private List<GoalValueDTO> values;
    private List<GoalSituationDTO> situations;
    private List<GoalBehaviorDTO> behaviors;
    private String content;
    private Long projectId;
}
