package org.example.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.example.model.request.UserReq;
import org.junit.jupiter.api.Test;

@Slf4j
public class SchemaGeneratorTest {

  @Test
  public void generateUserReqSchema() {
    Schema<?> schema = SchemaGenerator.generateSchema(UserReq.class);

    // 转换为JSON字符串
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    try {
      String schemaJson = mapper.writerWithDefaultPrettyPrinter()
          .writeValueAsString(schema);
      System.out.println(schemaJson);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage(), e);
    }
  }
}
