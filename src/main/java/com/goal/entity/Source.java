package com.goal.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Source {
    private String version;
    private String connector;
    private String name;
    private long ts_ms;
    private String snapshot;
    private String db;
    private String sequence;
    private String table;
    private long server_id;
    private String gtid;
    private String file;
    private long pos;
    private int row;
    private Long thread;
    private String query;
}
