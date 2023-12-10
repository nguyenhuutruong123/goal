package com.goal.graph.model;

import com.goal.graph.model.response.PageBodyOut;
import lombok.Data;

import java.util.List;

@Data
public class GraphResponse<T> {
    private PageBodyOut page;
    private List<T> results;
}
