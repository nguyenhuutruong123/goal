package com.goal.service.kafka;


import com.goal.common.errors.BusinessException;

import com.goal.common.utils.CommonDataUtil;
import com.goal.constants.GlobalConstant;
import com.goal.entity.*;
import com.goal.entity.dto.GoalBehaviorDTO;
import com.goal.entity.dto.GoalDTO;
import com.goal.entity.dto.GoalSituationDTO;
import com.goal.entity.dto.GoalValueDTO;
import com.goal.service.impl.ParentServiceImpl;
import com.google.gson.Gson;

import liquibase.pro.packaged.G;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumer {

    @Autowired
    ParentServiceImpl parentService;


    @KafkaListener(topicPartitions = @TopicPartition(topic = "json.be_account.goals", partitions = {"0"}), groupId = "myGroup")
    public void consumeGoals(ConsumerRecord<String, Object> record) {
        Gson gson = new Gson();
        try {
            String jsonString = record.value().toString();
            Message message = gson.fromJson(jsonString, Message.class);
            Value after = message.getPayload().getAfter();
            GoalDTO goal = new GoalDTO();
            CommonDataUtil.getModelMapper().map(after, goal);
            parentService.saveGoal(goal, GlobalConstant.TYPE_PARENT);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }
    @KafkaListener(topicPartitions = @TopicPartition(topic = "json.be_account.goals_goal_values", partitions = {"0"}), groupId = "myGroup")
    public void consumeGoalsValue(ConsumerRecord<String, Object> record) {
        Gson gson = new Gson();
        try {
            String jsonString = record.value().toString();
            Message message = gson.fromJson(jsonString, Message.class);
            Value after = message.getPayload().getAfter();
            GoalValueDTO result = new GoalValueDTO();
            CommonDataUtil.getModelMapper().map(after, result);
            parentService.saveGoal(result, GlobalConstant.TYPE_CHILD);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }
    @KafkaListener(topicPartitions = @TopicPartition(topic = "json.be_account.goals_goal_behaviors", partitions = {"0"}), groupId = "myGroup")
    public void consumeGoalsBehaviors(ConsumerRecord<String, Object> record) {
        Gson gson = new Gson();
        try {
            String jsonString = record.value().toString();
            Message message = gson.fromJson(jsonString, Message.class);
            Value after = message.getPayload().getAfter();
            GoalBehaviorDTO result = new GoalBehaviorDTO();
            CommonDataUtil.getModelMapper().map(after, result);
            parentService.saveGoal(result, GlobalConstant.TYPE_CHILD);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }
    @KafkaListener(topicPartitions = @TopicPartition(topic = "json.be_account.goals_goal_situations", partitions = {"0"}), groupId = "myGroup")
    public void consumeGoalsSituation(ConsumerRecord<String, Object> record) {
        Gson gson = new Gson();
        try {
            String jsonString = record.value().toString();
            Message message = gson.fromJson(jsonString, Message.class);
            Value after = message.getPayload().getAfter();
            GoalSituationDTO result = new GoalSituationDTO();
            CommonDataUtil.getModelMapper().map(after, result);
            parentService.saveGoal(result, GlobalConstant.TYPE_CHILD);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

}
