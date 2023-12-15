package com.goal.graph.model.request.goal;


import lombok.Data;

@Data
public class PageBody {
    private int page;
    private int size;

    public PageBody() {
        this.page = 0;
        this.size = 20;
    }
}
