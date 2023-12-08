package com.goal.entity;



import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Getter
@Setter
@Document(indexName = "goals_stagging")
public class Goal {

    @Field(name = "_id")
    private Long id;

    private String content;

    @Field(name = "created_by")
    private String createdBy;

    @Field(name = "created_date")
    private String createdDate;

    @Field(name = "modified_date")
    private String modifiedDate;

    @Field(name = "modified_by")
    private String modifiedBy;

    @Field(name = "project_id")
    private String projectId;

}
