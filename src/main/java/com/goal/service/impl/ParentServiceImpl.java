package com.goal.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.goal.common.utils.CommonDataUtil;
import com.goal.constants.GlobalConstant;
import com.goal.entity.dto.*;
import com.goal.service.ParentService;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.join.JoinField;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.goal.constants.GlobalConstant.*;

/**
 * Service Implementation for managing {@link ParentService}.
 */
@Service
public class ParentServiceImpl implements ParentService {

    private static final Logger log = LoggerFactory.getLogger(ParentServiceImpl.class);


    @Autowired
    private RestHighLevelClient client;


    public static Map<String, Object> convert(Object object) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(object);
            map.put(field.getName(), value);
        }

        return map;
    }

    public boolean createGoal(Object object) {
        GoalDTO goalStagingDTO = CommonDataUtil.getModelMapper().map(object, GoalDTO.class);
        ParentChildDTO parentChildDTO = new ParentChildDTO();
        parentChildDTO.setData(goalStagingDTO);
        try {
            Map<String, Object> sourceMap = convert(parentChildDTO);
            sourceMap.put("join_field", GlobalConstant.PARENT_GOAL);
            try {
                String json = CommonDataUtil.getModelMapperES().writeValueAsString(sourceMap);
                byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                // Now index the document
                IndexRequest indexRequest = new IndexRequest(GlobalConstant.INDEX_GOAL).id(goalStagingDTO.getId() + GlobalConstant.KEY_ID_GOAL + "")
                    .source(bytes, XContentType.JSON);
                client.index(indexRequest, RequestOptions.DEFAULT);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean createGoalValue(Object object) {

        GoalValueDTO input = CommonDataUtil.getModelMapper().map(object, GoalValueDTO.class);
        ParentChildDTO parentChildDTO = new ParentChildDTO();
        parentChildDTO.setData(input);
        try {
            Map<String, Object> sourceMap = convert(parentChildDTO);
            JoinField joinField = new JoinField(GlobalConstant.CHILD_GOAL_VALUE, input.getGoalId().toString() + GlobalConstant.KEY_ID_GOAL_VALUE);
            sourceMap.put("join_field", joinField);
            try {
                String json = CommonDataUtil.getModelMapperES().writeValueAsString(sourceMap);
                byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                // Now index the document
                IndexRequest indexRequest = new IndexRequest(GlobalConstant.INDEX_GOAL).id(input.getId() + GlobalConstant.KEY_ID_GOAL_VALUE)
                    .source(bytes, XContentType.JSON)
                    .routing(input.getGoalId() + "");
                client.index(indexRequest, RequestOptions.DEFAULT);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean createGoalBehavior(Object object) {

        GoalValueDTO input = CommonDataUtil.getModelMapper().map(object, GoalValueDTO.class);
        ParentChildDTO parentChildDTO = new ParentChildDTO();
        parentChildDTO.setData(input);
        try {
            Map<String, Object> sourceMap = convert(parentChildDTO);
            JoinField joinField = new JoinField(GlobalConstant.CHILD_GOAL_BEHAVIOR, input.getGoalId());
            sourceMap.put("join_field", joinField);
            try {
                String json = CommonDataUtil.getModelMapperES().writeValueAsString(sourceMap);
                byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                // Now index the document
                IndexRequest indexRequest = new IndexRequest(GlobalConstant.INDEX_GOAL).id(input.getId() + GlobalConstant.KEY_ID_GOAL_BEHAVIOR)
                    .source(bytes, XContentType.JSON)
                    .routing(input.getGoalId() + "");
                client.index(indexRequest, RequestOptions.DEFAULT);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean createGoalSituation(Object object) {

        GoalSituationDTO input = CommonDataUtil.getModelMapper().map(object, GoalSituationDTO.class);
        ParentChildDTO parentChildDTO = new ParentChildDTO();
        parentChildDTO.setData(input);
        try {
            Map<String, Object> sourceMap = convert(parentChildDTO);
            JoinField joinField = new JoinField(GlobalConstant.CHILD_GOAL_SITUATION, input.getGoalId());
            sourceMap.put("join_field", joinField);
            try {
                String json = CommonDataUtil.getModelMapperES().writeValueAsString(sourceMap);
                byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                // Now index the document
                IndexRequest indexRequest = new IndexRequest(GlobalConstant.INDEX_GOAL).id(input.getId() + GlobalConstant.KEY_ID_GOAL_SITUATION)
                    .source(bytes, XContentType.JSON)
                    .routing(input.getGoalId() + "");
                client.index(indexRequest, RequestOptions.DEFAULT);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean saveGoal(Object object,String type) throws IOException {
        if ((object instanceof GoalDTO) && type.equals(PARENT_GOAL)) {
            return createGoal(object);
        } else {
            if ((object instanceof GoalValueDTO)  && type.equals(CHILD_GOAL_VALUE)) {
                return createGoalValue(object);
            } else if ((object instanceof GoalBehaviorDTO)  && type.equals(CHILD_GOAL_BEHAVIOR)) {
                return createGoalBehavior(object);
            } else if ((object instanceof GoalSituationDTO)  && type.equals(CHILD_GOAL_SITUATION)) {
                return createGoalSituation(object);
            }
        }
        return true;
    }
}
