package com.goal.entity.dto;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GoalBehaviorDTO {
    @NotNull
    @NotEmpty
    @JsonProperty("id")
    private Long id;

    @NotNull
    @NotEmpty
    @JsonProperty("goal_id")
    private Long goalId;

    private String name;


    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_date")
    private String createdDate;

    @JsonProperty("modified_date")
    private String modifiedDate;

    @JsonProperty("modified_by")
    private String modifiedBy;
}
