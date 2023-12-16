package com.goal.common.constants;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.goal.common.constants.FormatConstants.GOAL_DATA_FIELD_FORMAT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ElasticSearchConstants {
    public static final String GOAL_INDEX_NAME = "staging_document_goal";
    public static final String GOAL_VALUE_INDEX_NAME = "goal_value";
    public static final String GOAL_BEHAVIOR_INDEX_NAME = "goal_behavior";
    public static final String GOAL_SITUATION_INDEX_NAME = "goal_situation";
    public static final String PARENT_GOAL = "goals";

    public static final String KEY_ID_GOAL = "_GOAL";
    public static final String KEY_ID_GOAL_VALUE = "_GOAL_VALUE";
    public static final String KEY_ID_GOAL_BEHAVIOR = "_GOAL_BEHAVIOR";
    public static final String KEY_ID_GOAL_SITUATION = "_GOAL_SITUATION";

    public static final String GOAL_SORT_FIELD = "data.id";
    public static final String NESTED_DATA_FIELD = "data";
    public static final String GOAL_ID_FIELD = "goal_id";
    public static final String NAME_FIELD = "name";
    public static final String JOIN_FIELD = "join_field";

    public static final String FILTER_GOAL_ID_FIELD = String.format(GOAL_DATA_FIELD_FORMAT, NESTED_DATA_FIELD, GOAL_ID_FIELD);
    public static final String FILTER_NAME_FIELD = String.format(GOAL_DATA_FIELD_FORMAT, NESTED_DATA_FIELD, NAME_FIELD);
}
