package com.goal.graph.model.response.goal;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoalSituationDTO {
    private Long id;
    private String name;
    @JsonProperty("created_by")
    private String createdBy;
    @JsonProperty("created_date")
    private String createdDate;
    @JsonProperty("modified_by")
    private String modifiedBy;
    @JsonProperty("modified_date")
    private String modifiedDate;
}
