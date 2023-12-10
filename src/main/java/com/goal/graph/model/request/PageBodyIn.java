package com.goal.graph.model.request;


import lombok.Data;

@Data
public class PageBodyIn {
    private int size;
    private int number;
    private int sortType;
    private int sorOrder;
}
