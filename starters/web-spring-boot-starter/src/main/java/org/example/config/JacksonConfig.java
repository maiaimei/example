package org.example.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import org.example.constants.DateConstants;
import org.example.utils.JsonUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  @PostConstruct
  public void setUp() {
    // TODO: JsonUtils.setObjectMapper(new ObjectMapper());
    JsonUtils.setObjectMapper(new ObjectMapper());
  }

  /**
   * 配置序列化特性
   *
   * @return Jackson2ObjectMapperBuilderCustomizer
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer serializationFeaturesCustomizer() {
    return builder -> {
      builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
      builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    };
  }

  /**
   * 配置反序列化特性
   *
   * @return Jackson2ObjectMapperBuilderCustomizer
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer deserializationFeaturesCustomizer() {
    return builder -> {
      builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      builder.featuresToEnable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    };
  }

  /**
   * 配置日期时间格式
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer dateTimeCustomizer() {
    return builder -> builder
        // 设置时区为UTC
        .timeZone(TimeZone.getTimeZone(ZoneId.of("UTC")))
        // 设置格式为UTC
        .simpleDateFormat(DateConstants.DATETIME_UTC_FORMAT)
        // 禁用将日期写为时间戳
        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        // 配置日期时间序列化器
        .serializers(
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateConstants.DATETIME_UTC_FORMAT)),
            new LocalDateSerializer(DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT)),
            new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateConstants.TIME_FORMAT))
        )
        // 配置日期时间反序列化器
        .deserializers(
            new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateConstants.DATETIME_UTC_FORMAT)),
            new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateConstants.DATE_FORMAT)),
            new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateConstants.TIME_FORMAT))
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
