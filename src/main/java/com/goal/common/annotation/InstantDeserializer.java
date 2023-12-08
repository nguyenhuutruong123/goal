package com.goal.common.annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.goal.common.utils.CommonDataUtil;
import com.goal.common.utils.DateUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class InstantDeserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonToken currentToken = jsonParser.getCurrentToken();
        if (JsonToken.VALUE_STRING.equals(currentToken)) {
            String value = jsonParser.getValueAsString();
            Date date = DateUtils.formatDateTime(value);
            return CommonDataUtil.isNotNull(date) ? date.toInstant() : null;
        }
        return jsonParser.readValueAs(Instant.class);
    }
}
