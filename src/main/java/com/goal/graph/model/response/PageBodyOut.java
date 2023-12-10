package com.goal.graph.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageBodyOut {
    private Integer number;
    private Integer size;
    private Integer total;
    private Long count;
}
