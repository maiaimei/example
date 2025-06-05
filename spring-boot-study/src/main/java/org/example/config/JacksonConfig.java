package org.example.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import org.example.constant.DatePattern;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  /**
   * 配置日期时间格式
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer dateTimeCustomizer() {
    return builder -> builder
        // 设置时区为UTC
        .timeZone(TimeZone.getTimeZone(ZoneId.of("UTC")))
        // 设置格式为UTC
        .simpleDateFormat(DatePattern.DATETIME_UTC_FORMAT)
        // 禁用将日期写为时间戳
        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        // 配置日期时间序列化器
        .serializers(
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.DATETIME_UTC_FORMAT)),
            new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.DATE_FORMAT)),
            new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.TIME_FORMAT))
        )
        // 配置日期时间反序列化器
        .deserializers(
            new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.DATETIME_UTC_FORMAT)),
            new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.DATE_FORMAT)),
            new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.TIME_FORMAT))
        );
  }

  /**
   * 配置数值类型的序列化
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer numberCustomizer() {
    return builder -> builder
        .serializerByType(BigDecimal.class, new ToStringSerializer());
  }
}
