package com.goal.service.impl;

import co.elastic.clients.elasticsearch._types.mapping.SourceField;
import co.elastic.clients.elasticsearch.indices.PutMappingResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.goal.common.utils.CommonDataUtil;
import com.goal.constants.GlobalConstant;
import com.goal.entity.dto.*;
import com.goal.service.ParentService;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.join.JoinField;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
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

    public boolean createGoal(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // Disable timestamp-based serialization
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Disable unknown property errors
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
                IndexRequest indexRequest = new IndexRequest(GlobalConstant.INDEX_GOAL).id(goalStagingDTO.getId() + GlobalConstant.KEY_ID_GOAL + "")
                    .source(bytes, XContentType.JSON);
                IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
                System.out.println(response);
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
    public boolean saveGoal(Object object, String type) throws IOException {

        if (type.equals(GlobalConstant.TYPE_PARENT)) {
            if ((object instanceof GoalDTO)) {
                return createGoal(object);
            } else {
                if ((object instanceof GoalValueDTO)) {
                    return createGoalValue(object);
                } else if ((object instanceof GoalBehaviorDTO)) {
                    return createGoalBehavior(object);
                } else if ((object instanceof GoalSituationDTO)) {
                    return createGoalSituation(object);
                }
            }
            return false;
        }
        return true;
    }
}
