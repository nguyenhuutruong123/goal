package com.goal.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.goal.common.utils.CommonDataUtil;
import com.goal.constants.GlobalConstant;
import com.goal.entity.dto.*;
import com.goal.service.ParentService;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
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

    @Override
    public boolean saveGoal(Object object, String type) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // Disable timestamp-based serialization
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Disable unknown property errors
        if (type.equals(GlobalConstant.TYPE_PARENT)) {
            if ((object instanceof GoalDTO)) {
                GoalDTO goalStagingDTO = CommonDataUtil.getModelMapper().map(object, GoalDTO.class);
                ParentChildDTO parentChildDTO = new ParentChildDTO();
                parentChildDTO.setData(goalStagingDTO);
                try {
                    Map<String, Object> sourceMap = convert(parentChildDTO);
                    sourceMap.put("join_field", GlobalConstant.PARENT_GOAL);
                    try {
                        String json = objectMapper.writeValueAsString(sourceMap);
                        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                        // Now index the document
                        IndexRequest indexRequest = new IndexRequest(GlobalConstant.INDEX_GOAL).id(goalStagingDTO.getId()+GlobalConstant.KEY_ID_GOAL + "")
                            .source(bytes, XContentType.JSON);
                        client.index(indexRequest, RequestOptions.DEFAULT);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            if ((object instanceof GoalValueDTO)) {
                GoalValueDTO input = CommonDataUtil.getModelMapper().map(object, GoalValueDTO.class);
                ParentChildDTO parentChildDTO = new ParentChildDTO();
                parentChildDTO.setData(input);
                try {
                    Map<String, Object> sourceMap = convert(parentChildDTO);
                    JoinField joinField = new JoinField(GlobalConstant.CHILD_GOAL_VALUE,input.getGoalId());
                    sourceMap.put("join_field", joinField);
                    try {
                        String json = objectMapper.writeValueAsString(sourceMap);
                        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                        // Now index the document
                        IndexRequest indexRequest = new IndexRequest(GlobalConstant.INDEX_GOAL).id(input.getId() + GlobalConstant.KEY_ID_GOAL_VALUE)
                            .source(bytes, XContentType.JSON)
                            .routing(input.getGoalId()+"");
                        client.index(indexRequest, RequestOptions.DEFAULT);

                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }else  if ((object instanceof GoalBehaviorDTO)) {
                GoalValueDTO input = CommonDataUtil.getModelMapper().map(object, GoalValueDTO.class);
                ParentChildDTO parentChildDTO = new ParentChildDTO();
                parentChildDTO.setData(input);
                try {
                    Map<String, Object> sourceMap = convert(parentChildDTO);
                    JoinField joinField = new JoinField(GlobalConstant.CHILD_GOAL_BEHAVIOR,input.getGoalId());
                    sourceMap.put("join_field", joinField);
                    try {
                        String json = objectMapper.writeValueAsString(sourceMap);
                        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                        // Now index the document
                        IndexRequest indexRequest = new IndexRequest(GlobalConstant.INDEX_GOAL).id(input.getId() + GlobalConstant.KEY_ID_GOAL_BEHAVIOR)
                            .source(bytes, XContentType.JSON)
                            .routing(input.getGoalId()+"");
                       client.index(indexRequest, RequestOptions.DEFAULT);

                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }else  if ((object instanceof GoalSituationDTO)) {
                GoalSituationDTO input = CommonDataUtil.getModelMapper().map(object, GoalSituationDTO.class);
                ParentChildDTO parentChildDTO = new ParentChildDTO();
                parentChildDTO.setData(input);
                try {
                    Map<String, Object> sourceMap = convert(parentChildDTO);
                    JoinField joinField = new JoinField(GlobalConstant.CHILD_GOAL_SITUATION,input.getGoalId());
                    sourceMap.put("join_field", joinField);
                    try {
                        String json = objectMapper.writeValueAsString(sourceMap);
                        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                        // Now index the document
                        IndexRequest indexRequest = new IndexRequest(GlobalConstant.INDEX_GOAL).id(input.getId() + GlobalConstant.KEY_ID_GOAL_SITUATION)
                            .source(bytes, XContentType.JSON)
                            .routing(input.getGoalId()+"");
                       client.index(indexRequest, RequestOptions.DEFAULT);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }

}
