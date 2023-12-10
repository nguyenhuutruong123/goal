package com.goal.graph.config.scalar;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import org.jetbrains.annotations.NotNull;

public class LongScalar {
    public GraphQLScalarType longScalar() {
        return GraphQLScalarType.newScalar()
                .name("Long")
                .description("Java 11 Long as scalar.")
                .coercing(new Coercing<Long, String>() {
                    @Override
                    public String serialize(@NotNull final Object dataFetcherResult) {
                        if (dataFetcherResult instanceof Long) {
                            return dataFetcherResult.toString();
                        } else {
                            throw new CoercingSerializeException("Expected a Long object.");
                        }
                    }

                    @NotNull
                    @Override
                    public Long parseValue(@NotNull final Object input) {
                        if (input instanceof Long) {
                            return (Long) input;
                        } else if (input instanceof String) {
                            return Long.parseLong(((String) input));
                        } else {
                            throw new CoercingParseValueException("Expected a String");
                        }
                    }

                    @NotNull
                    @Override
                    public Long parseLiteral(@NotNull final Object input) {
                        if (input instanceof Long) {
                            return (Long) input;
                        } else if (input instanceof StringValue) {
                            return Long.parseLong(((StringValue) input).getValue());
                        } else {
                            throw new CoercingParseLiteralException("Expected a StringValue.");
                        }
                    }
                }).build();
    }
}
