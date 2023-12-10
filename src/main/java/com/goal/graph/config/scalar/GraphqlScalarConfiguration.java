package com.goal.graph.config.scalar;

import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphqlScalarConfiguration {

    @Bean
    public GraphQLScalarType longScalar() {
        return new LongScalar().longScalar();
    }
}
