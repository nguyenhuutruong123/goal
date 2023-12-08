package com.goal.service.impl;


import com.goal.entity.dto.RequestDTO;
import com.goal.service.SearchService;

import graphql.GraphQL;
import graphql.execution.AsyncExecutionStrategy;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.join.query.JoinQueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;


import java.io.IOException;


/**
 * Service Implementation for managing {@link GoalsService}.
 */
@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private RestHighLevelClient client;

    @Override
    public SearchResponse getDataIndex(RequestDTO request) throws IOException {
        SearchRequest searchRequest = new SearchRequest(request.getIndex());
        searchRequest.source(new SearchSourceBuilder()
            .query(QueryBuilders.boolQuery()
                .must(QueryBuilders.matchAllQuery())
                .filter(JoinQueryBuilders.hasChildQuery(
                    "goal_value_child",
                    QueryBuilders.matchAllQuery(),
                    ScoreMode.None))
            )
            .size(10) // adjust as needed
            .timeout(TimeValue.timeValueSeconds(10))); // ad
        return client.search(searchRequest, RequestOptions.DEFAULT);

    }

    public void searchData(RequestDTO request) {
        // Set up GraphQL schema and resolvers
        String graphqlSchema = "type Query { searchElasticsearch(query: String): [String] }";
        GraphQLSchema schema = buildSchema(graphqlSchema);
        // Run a sample GraphQL query
        String query = "{ searchElasticsearch(query: \"your_query_here\") }";
        executeQuery(schema, query, client);
        // Close Elasticsearch client
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GraphQLSchema buildSchema(String graphqlSchema) {
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
            .type("Query", builder -> builder.dataFetcher("searchElasticsearch", getElasticsearchDataFetcher()))
            .build();

        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(schemaParser.parse(graphqlSchema), runtimeWiring);
    }

    public DataFetcher<?> getElasticsearchDataFetcher() {
        return environment -> {
            String query = environment.getArgument("query");

            // Execute Elasticsearch query (replace with your actual Elasticsearch query logic)
            SearchRequest searchRequest = new SearchRequest("your_index_name");
            searchRequest.source().query(QueryBuilders.queryStringQuery(query));

            // Execute Elasticsearch query and return results
            // Replace with actual Elasticsearch query execution logic
            // For simplicity, returning a list of strings
            return client.search(searchRequest, RequestOptions.DEFAULT).toString();
        };
    }

    public void executeQuery(GraphQLSchema schema, String query, RestHighLevelClient esClient) {
        GraphQL graphQL = GraphQL.newGraphQL(schema)
            .queryExecutionStrategy(new AsyncExecutionStrategy())
            .build();

        System.out.println(graphQL.execute(query).toSpecification());
    }
}
