package com.goal.entity;



import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;


@Getter
@Setter
@Document(indexName = "goal_value_situations")
public class GoalSituations {
    @Field(name = "_id")
    private Long id;

    @Field(name = "goal_id")
    private Long goalId;

    private String name;


    @Field(name = "created_by")
    private String createdBy;

    @Field(name = "created_date")
    private String createdDate;

    @Field(name = "modified_date")
    private String modifiedDate;

    @Field(name = "modified_by")
    private String modifiedBy;
}
