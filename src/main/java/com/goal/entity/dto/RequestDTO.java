package com.goal.entity.dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDTO {

    private String index;

    private String parent_type  ;

    private String child_type  ;

    private String field_name  ;
    private Integer goal_id  ;

    private String value_to_match   ;

}
