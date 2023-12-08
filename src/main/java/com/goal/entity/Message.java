package com.goal.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    @JsonIgnoreProperties
    private Envelope schema;
    @JsonIgnoreProperties
    private Payload payload;
}
