package com.core.config.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value != null) {
            BigDecimal num = value.setScale(3, RoundingMode.HALF_UP);
            jgen.writeString(num.toString() == null ? "0.000" : num.toString());
        } else {
            jgen.writeString("0.000");
        }
    }
}