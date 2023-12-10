package com.goal.graph.model.response.goal;


import lombok.Data;

@Data
public class GoalValueDTO {
    private Long id;
    private String value;
    private Long goalId;
    private String name;
    private String type;
}
