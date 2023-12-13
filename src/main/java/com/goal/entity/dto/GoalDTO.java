package com.goal.entity.dto;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GoalDTO {

    @NotNull
    @JsonProperty("id")
    private Long id;

    private String content;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_date")
    private String createdDate;

    @JsonProperty("modified_date")
    private String modifiedDate;

    @JsonProperty("modified_by")
    private String modifiedBy;

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("situation_response")
    private String situationResponse;

    @JsonProperty("encounter")
    private String encounter;

    @JsonProperty("encounter_response")
    private String encounterResponse;

    @JsonProperty("organization_id")
    private String organizationId;

}
