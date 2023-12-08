package com.goal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;

import java.time.Instant;

@Getter
@Setter
public class Value {
    private int id;
    private String name;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String content;
    @JsonProperty("created_by")
    private String createdBy;
    @JsonProperty("created_date")
    private Instant createdDate;
    @JsonProperty("modified_date")
    private Instant modifiedDate;
    @JsonProperty("modified_by")
    private String modifiedBy;
    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty("goal_id")
    private Long goalId;
    private String type;
}
