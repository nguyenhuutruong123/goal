package com.goal.entity.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.join.JoinField;

@Getter
@Setter
public class ParentDTO {

    private Object data;

    private String join_field;

}
