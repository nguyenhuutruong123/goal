package com.goal.graph.model.response.goal;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoalSituationDTO {
    private Long id;
    private Long goalId;
    private String situation;
    private String name;
}
