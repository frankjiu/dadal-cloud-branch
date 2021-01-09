package com.core.config.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.IsoFields;
import static com.core.constant.Constant.TIME_ZONE;

/**
 * 将long类型转换为指定的时间字符串
 * 并且将在属性中增加一个woy(WeekOfYear)属性表示该时间是该年的第几周
 * 格式为"年份W周"
 * 例如2018年第3周将为 2018W3
 */
public class LongToDateWithWeekSerializer extends JsonSerializer<Long> {
    @Override
    public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(value),TIME_ZONE);
        DateTimeFormatter format = new DateTimeFormatterBuilder()
                .appendValue(IsoFields.WEEK_BASED_YEAR)
                .appendLiteral("W")
                .appendValue(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                .toFormatter();
        String strTime = dateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        jgen.writeString(strTime);
        jgen.writeStringField("woy",format.format(dateTime));
    }
}