package com.core.config.serializer;

import com.core.constant.Constant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.core.constant.Constant.TIME_ZONE;

/**
 * 将long类型转换为指定的时间字符串
 */
public class LongToDateSerializer extends JsonSerializer<Long> {
    @Override
    public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(value), TIME_ZONE);
        String strTime = dateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        jgen.writeString(strTime);
    }
}