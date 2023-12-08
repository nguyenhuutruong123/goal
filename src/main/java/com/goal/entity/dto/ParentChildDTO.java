package com.goal.entity.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.join.JoinField;

@Getter
@Setter
public class ParentChildDTO {

    private Object data;
    @Field(type = FieldType.Object, name = "join_field")
    private JoinField joinField;

}
