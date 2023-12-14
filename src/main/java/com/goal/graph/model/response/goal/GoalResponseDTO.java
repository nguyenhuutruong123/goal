package com.goal.graph.model.response.goal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GoalResponseDTO {
    private Long id;
    private String content;
    @JsonProperty("situation_response")
    private String situationResponse;
    private String encounter;
    @JsonProperty("encounter_response")
    private String encounterResponse;
    @JsonProperty("project_id")
    private Long projectId;
    @JsonProperty("created_by")
    private String createdBy;
    @JsonProperty("created_date")
    private String createdDate;
    @JsonProperty("modified_date")
    private String modifiedDate;
    @JsonProperty("modified_by")
    private String modifiedBy;
    @JsonProperty("organization_d")
    private Long organizationId;
    private List<GoalBehaviorDTO> goalBehaviours;
    private List<GoalValueDTO> goalValues;
    private List<GoalSituationDTO> goalSituations;
}
