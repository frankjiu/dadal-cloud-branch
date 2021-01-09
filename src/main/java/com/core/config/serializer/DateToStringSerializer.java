package com.core.config.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.core.constant.Constant.TIME_ZONE;

public class DateToStringSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(value.getTime() / 1000), TIME_ZONE);
        String strTime = dateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        gen.writeString(strTime);
    }
}
