package com.goal.graph.model.response.goal;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoalBehaviorDTO {
    private Long id;
    private Long goalId;
    private String name;
    private String type;
    private String behavior;
}
