package com.goal.graph.model.request.goal;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
public class SearchGoalFilter {
    private List<Long> goalIds;
}
