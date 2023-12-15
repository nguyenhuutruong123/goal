package com.goal.service.kafka;


import com.goal.common.errors.BusinessException;
import com.goal.common.utils.ProcessorDataMapper;
import com.goal.elasticsearch.service.ElasticSearchGoalService;
import com.goal.entity.Message;
import com.goal.entity.Value;
import com.goal.entity.dto.GoalBehaviorDTO;
import com.goal.entity.dto.GoalDTO;
import com.goal.entity.dto.GoalSituationDTO;
import com.goal.entity.dto.GoalValueDTO;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import static com.goal.common.constants.ElasticSearchConstants.GOAL_BEHAVIOR_INDEX_NAME;
import static com.goal.common.constants.ElasticSearchConstants.GOAL_SITUATION_INDEX_NAME;
import static com.goal.common.constants.ElasticSearchConstants.GOAL_VALUE_INDEX_NAME;
import static com.goal.common.constants.ElasticSearchConstants.PARENT_GOAL;


@Service
@AllArgsConstructor
public class KafkaConsumer {
    private final ElasticSearchGoalService elasticSearchGoalService;
    private final ProcessorDataMapper dataMapper;


   // @KafkaListener(topicPartitions = @TopicPartition(topic = "json.be_account.goals", partitions = {"0"}), groupId = "myGroup")
    public void consumeGoals(ConsumerRecord<String, Object> consumerRecord) {
        Gson gson = new Gson();
        try {
            String jsonString = consumerRecord.value().toString();
            Message message = gson.fromJson(jsonString, Message.class);
            Value after = message.getPayload().getAfter();
            GoalDTO goal = new GoalDTO();
            dataMapper.map(after, goal);
            elasticSearchGoalService.saveGoal(goal, PARENT_GOAL);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

   // @KafkaListener(topicPartitions = @TopicPartition(topic = "json.be_account.goals_goal_values", partitions = {"0"}), groupId = "myGroup")
    public void consumeGoalsValue(ConsumerRecord<String, Object> consumerRecord) {
        Gson gson = new Gson();
        try {
            String jsonString = consumerRecord.value().toString();
            Message message = gson.fromJson(jsonString, Message.class);
            Value after = message.getPayload().getAfter();
            GoalValueDTO result = new GoalValueDTO();
            dataMapper.map(after, result);
            elasticSearchGoalService.saveGoal(result, GOAL_VALUE_INDEX_NAME);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

   // @KafkaListener(topicPartitions = @TopicPartition(topic = "json.be_account.goals_goal_behaviors", partitions = {"0"}), groupId = "myGroup")
    public void consumeGoalsBehaviors(ConsumerRecord<String, Object> consumerRecord) {
        Gson gson = new Gson();
        try {
            String jsonString = consumerRecord.value().toString();
            Message message = gson.fromJson(jsonString, Message.class);
            Value after = message.getPayload().getAfter();
            GoalBehaviorDTO result = new GoalBehaviorDTO();
            dataMapper.map(after, result);
            elasticSearchGoalService.saveGoal(result, GOAL_BEHAVIOR_INDEX_NAME);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

   // @KafkaListener(topicPartitions = @TopicPartition(topic = "json.be_account.goals_goal_situations", partitions = {"0"}), groupId = "myGroup")
    public void consumeGoalsSituation(ConsumerRecord<String, Object> consumerRecord) {
        Gson gson = new Gson();
        try {
            String jsonString = consumerRecord.value().toString();
            Message message = gson.fromJson(jsonString, Message.class);
            Value after = message.getPayload().getAfter();
            GoalSituationDTO result = new GoalSituationDTO();
            dataMapper.map(after, result);
            elasticSearchGoalService.saveGoal(result, GOAL_SITUATION_INDEX_NAME);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

}
