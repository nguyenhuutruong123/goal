package com.goal.elasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.NestedSortValue;
import co.elastic.clients.elasticsearch._types.SortMode;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.InnerHitsResult;
import co.elastic.clients.util.ObjectBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goal.common.utils.ProcessorDataMapper;
import com.goal.elasticsearch.model.ElasticSearchSourceDTO;
import com.goal.graph.model.response.goal.GoalBehaviorDTO;
import com.goal.graph.model.response.goal.GoalResponseDTO;
import com.goal.graph.model.response.goal.GoalSituationDTO;
import com.goal.graph.model.response.goal.GoalValueDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.goal.common.constants.Constant.BRACES;
import static com.goal.common.constants.ElasticSearchConstants.GOAL_BEHAVIOR_INDEX_NAME;
import static com.goal.common.constants.ElasticSearchConstants.GOAL_INDEX_NAME;
import static com.goal.common.constants.ElasticSearchConstants.GOAL_SITUATION_INDEX_NAME;
import static com.goal.common.constants.ElasticSearchConstants.GOAL_SORT_FIELD;
import static com.goal.common.constants.ElasticSearchConstants.GOAL_VALUE_INDEX_NAME;
import static com.goal.common.constants.ElasticSearchConstants.NESTED_DATA_FIELD;


@Slf4j
@Service
@AllArgsConstructor
public class ElasticSearchGoalServiceImpl implements ElasticSearchGoalService {
    private ElasticsearchClient elasticsearchClient;
    private ProcessorDataMapper dataMapper;
    private ObjectMapper objectMapper;


    @Override
    public List<GoalResponseDTO> getAllGoals(Integer page, Integer size) throws IOException {
        SearchRequest searchRequest = buildSearchAllGoalRequest().apply(page, size);
        SearchResponse<Object> searchResponse = elasticsearchClient.search(searchRequest, Object.class);

        return searchResponse.hits().hits().stream()
            .map(h -> buildGoalResponse().apply(h))
            .collect(Collectors.toList());
    }

    private BiFunction<Integer, Integer, SearchRequest> buildSearchAllGoalRequest() {
        return (page, size) -> {
            int from = page * size;
            return SearchRequest.of(s -> s
                .index(GOAL_INDEX_NAME)
                .query(buildQuerySearchGoalFunc())
                .sort(buildSortFunc())
                .from(from)
                .size(size)
            );
        };
    }

    private Function<SortOptions.Builder, ObjectBuilder<SortOptions>> buildSortFunc() {
        return sort -> sort.field(sf -> sf.field(GOAL_SORT_FIELD)
            .order(SortOrder.Asc)
            .mode(SortMode.Min)
            .nested(NestedSortValue.of(nsv -> nsv.path(NESTED_DATA_FIELD)))
        );
    }

    private Function<Query.Builder, ObjectBuilder<Query>> buildQuerySearchGoalFunc() {
        return q -> q.bool(b ->
                b.must(buildMustBuilderFunc(GOAL_VALUE_INDEX_NAME))
                    .must(buildMustBuilderFunc(GOAL_BEHAVIOR_INDEX_NAME))
                    .must(buildMustBuilderFunc(GOAL_SITUATION_INDEX_NAME))
        );
    }

    private Function<Query.Builder, ObjectBuilder<Query>> buildMustBuilderFunc(String index) {
        return m -> m.hasChild(hc ->
                hc.type(index)
                    .query(hcq -> hcq.matchAll(ma -> ma.queryName(StringUtils.EMPTY)))
                    .innerHits(inh -> inh.withJson(new StringReader(BRACES)))
        );
    }

    private Function<Hit<Object>, GoalResponseDTO> buildGoalResponse() {
        return hit -> {
            ElasticSearchSourceDTO elasticSearchSource = dataMapper.map(hit.source(), ElasticSearchSourceDTO.class);
            GoalResponseDTO goalResponse = parseObjectDTO(elasticSearchSource.getData(), GoalResponseDTO.class);

            Map<String, InnerHitsResult> innerHitsMap = hit.innerHits();
            buildGoalChild(innerHitsMap).accept(goalResponse);
            return goalResponse;
        };
    }

    private Consumer<GoalResponseDTO> buildGoalChild(Map<String, InnerHitsResult> innerHitsMap) {
        return goalResponse -> {
            List<GoalValueDTO> goalValues = buildGoalValues(innerHitsMap.get(GOAL_VALUE_INDEX_NAME), GoalValueDTO.class);
            List<GoalBehaviorDTO> goalBehaviors = buildGoalValues(innerHitsMap.get(GOAL_BEHAVIOR_INDEX_NAME), GoalBehaviorDTO.class);
            List<GoalSituationDTO> goalSituations = buildGoalValues(innerHitsMap.get(GOAL_SITUATION_INDEX_NAME), GoalSituationDTO.class);

            goalResponse.setGoalValues(goalValues);
            goalResponse.setGoalBehaviours(goalBehaviors);
            goalResponse.setGoalSituations(goalSituations);
        };
    }

    private <T> List<T> buildGoalValues(InnerHitsResult innerHitsResult, Class<T> esClazz) {
        return innerHitsResult.hits().hits()
            .stream().map(Hit::source)
            .filter(Objects::nonNull)
            .map(s -> dataMapper.map(s.toJson(), ElasticSearchSourceDTO.class))
            .map(ElasticSearchSourceDTO::getData)
            .map(data -> parseObjectDTO(data, esClazz))
            .collect(Collectors.toList());
    }

    private <T> T parseObjectDTO(Object obj, Class<T> clazz) {
        try {
            if (obj instanceof LinkedHashMap) {
                return objectMapper.readValue(objectMapper.writeValueAsString(obj), clazz);
            }
            return objectMapper.readValue(obj.toString(), clazz);
        } catch (JsonProcessingException e) {
            log.error("Parse object to {} failed.", clazz);
            throw new RuntimeException();
        }
    }
}
