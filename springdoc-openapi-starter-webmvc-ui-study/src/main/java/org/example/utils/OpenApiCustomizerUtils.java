package org.example.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class OpenApiCustomizerUtils {

  public static JsonNode loadConfiguration(String path) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      Resource resource = new ClassPathResource(path);
      return mapper.readTree(resource.getInputStream());
    } catch (IOException e) {
      throw new RuntimeException("Failed to load OpenAPI configuration", e);
    }
  }

  public static void configureOpenApi(OpenAPI openApi, JsonNode config) {
    // 配置基本信息
    configureInfo(openApi, config.get("info"));
    // 配置路径
    configurePaths(openApi, config.get("paths"));
  }

  public static void configureOpenApiPathsOnly(OpenAPI openApi, JsonNode config) {
    // 配置路径
    configurePaths(openApi, config.get("paths"));
  }

  public static void configureInfo(OpenAPI openApi, JsonNode infoNode) {
    if (infoNode != null) {
      Info info = new Info()
          .title(getStringValue(infoNode, "title"))
          .version(getStringValue(infoNode, "version"))
          .description(getStringValue(infoNode, "description"));

      JsonNode contactNode = infoNode.get("contact");
      if (contactNode != null) {
        info.contact(new Contact()
            .name(getStringValue(contactNode, "name"))
            .email(getStringValue(contactNode, "email")));
      }

      openApi.info(info);
    }
  }

  public static void configurePaths(OpenAPI openApi, JsonNode pathsNode) {
    if (pathsNode == null) {
      return;
    }

    Paths paths = openApi.getPaths();
    pathsNode.fields().forEachRemaining(entry -> {
      String pathUrl = entry.getKey();
      JsonNode pathItemNode = entry.getValue();
      PathItem pathItem = createPathItem(pathItemNode);
      paths.addPathItem(pathUrl, pathItem);
    });

    openApi.setPaths(paths);
  }

  public static PathItem createPathItem(JsonNode pathItemNode) {
    PathItem pathItem = new PathItem();

    // 处理不同的HTTP方法
    if (pathItemNode.has("get")) {
      pathItem.get(createOperation(pathItemNode.get("get")));
    }
    if (pathItemNode.has("post")) {
      pathItem.post(createOperation(pathItemNode.get("post")));
    }
    if (pathItemNode.has("put")) {
      pathItem.put(createOperation(pathItemNode.get("put")));
    }
    if (pathItemNode.has("delete")) {
      pathItem.delete(createOperation(pathItemNode.get("delete")));
    }

    return pathItem;
  }

  public static Operation createOperation(JsonNode operationNode) {
    Operation operation = new Operation();

    // 设置基本信息
    operation.summary(getStringValue(operationNode, "summary"))
        .description(getStringValue(operationNode, "description"));

    // 设置标签
    JsonNode tagsNode = operationNode.get("tags");
    if (tagsNode != null && tagsNode.isArray()) {
      List<String> tags = new ArrayList<>();
      tagsNode.forEach(tag -> tags.add(tag.asText()));
      operation.setTags(tags);
    }

    // 设置请求体
    JsonNode requestBodyNode = operationNode.get("requestBody");
    if (requestBodyNode != null) {
      operation.requestBody(createRequestBody(requestBodyNode));
    }

    // 设置响应
    JsonNode responsesNode = operationNode.get("responses");
    if (responsesNode != null) {
      operation.responses(createApiResponses(responsesNode));
    }

    return operation;
  }

  public static RequestBody createRequestBody(JsonNode requestBodyNode) {
    RequestBody requestBody = new RequestBody();
    requestBody.required(getBooleanValue(requestBodyNode, "required", false));

    JsonNode contentNode = requestBodyNode.get("content");
    if (contentNode != null) {
      Content content = new Content();
      contentNode.fields().forEachRemaining(entry -> {
        String mediaType = entry.getKey();
        JsonNode mediaTypeNode = entry.getValue();
        content.addMediaType(mediaType, createMediaType(mediaTypeNode));
      });
      requestBody.content(content);
    }

    return requestBody;
  }

  public static MediaType createMediaType(JsonNode mediaTypeNode) {
    MediaType mediaType = new MediaType();
    JsonNode schemaNode = mediaTypeNode.get("schema");
    if (schemaNode != null) {
      mediaType.schema(createSchema(schemaNode));
    }
    return mediaType;
  }

  public static Schema<?> createSchema(JsonNode schemaNode) {
    Schema<?> schema = new Schema<>();

    schema.type(getStringValue(schemaNode, "type"));
    schema.description(getStringValue(schemaNode, "description"));
    schema.example(schemaNode.get("example"));

    // 处理属性
    JsonNode propertiesNode = schemaNode.get("properties");
    if (propertiesNode != null) {
      propertiesNode.fields().forEachRemaining(entry -> {
        String propertyName = entry.getKey();
        JsonNode propertyNode = entry.getValue();
        schema.addProperties(propertyName, createSchema(propertyNode));
      });
    }

    return schema;
  }

  public static ApiResponses createApiResponses(JsonNode responsesNode) {
    ApiResponses responses = new ApiResponses();
    responsesNode.fields().forEachRemaining(entry -> {
      String statusCode = entry.getKey();
      JsonNode responseNode = entry.getValue();
      responses.addApiResponse(statusCode, createApiResponse(responseNode));
    });
    return responses;
  }

  public static ApiResponse createApiResponse(JsonNode responseNode) {
    ApiResponse response = new ApiResponse();
    response.description(getStringValue(responseNode, "description"));

    JsonNode contentNode = responseNode.get("content");
    if (contentNode != null) {
      Content content = new Content();
      contentNode.fields().forEachRemaining(entry -> {
        String mediaType = entry.getKey();
        JsonNode mediaTypeNode = entry.getValue();
        content.addMediaType(mediaType, createMediaType(mediaTypeNode));
      });
      response.content(content);
    }

    return response;
  }

  public static String getStringValue(JsonNode node, String field) {
    JsonNode fieldNode = node.get(field);
    return fieldNode != null ? fieldNode.asText() : null;
  }

  public static boolean getBooleanValue(JsonNode node, String field, boolean defaultValue) {
    JsonNode fieldNode = node.get(field);
    return fieldNode != null ? fieldNode.asBoolean() : defaultValue;
  }
}
