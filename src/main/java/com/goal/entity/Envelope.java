package com.goal.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Envelope {
    @JsonIgnoreProperties
    private Value before;
    @JsonIgnoreProperties
    private Value after;
    @JsonIgnoreProperties
    private Source source;
    private String op;
    private long ts_ms;
    @JsonIgnoreProperties
    private EventBlock transaction;
}
