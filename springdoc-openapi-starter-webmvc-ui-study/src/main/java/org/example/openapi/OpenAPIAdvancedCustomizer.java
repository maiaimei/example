package org.example.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.PathItem.HttpMethod;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAPIAdvancedCustomizer {

  @Value("${springdoc.methods-to-match:GET,POST,PUT,DELETE,PATCH,HEAD,OPTIONS,TRACE}")
  private String methodsToMatch;

  public void customise(OpenAPI openAPI) {
    // 1. 首先过滤路径和方法
    filterPathsByMethods(openAPI);

    // 2. 收集保留的操作中使用的tags和schemas
    Set<String> usedTags = new HashSet<>();
    Set<String> usedSchemas = new HashSet<>();
    collectUsedTagsAndSchemas(openAPI, usedTags, usedSchemas);

    // 3. 过滤tags
    filterUnusedTags(openAPI, usedTags);

    // 4. 过滤schemas
    filterUnusedSchemas(openAPI, usedSchemas);
  }

  private void filterPathsByMethods(OpenAPI openAPI) {
    final Paths paths = openAPI.getPaths();
    Set<HttpMethod> allowedMethods = getAllowedMethods();

    paths.forEach((pathUrl, pathItem) -> {
      if (pathItem != null) {
        filterMethods(pathItem, allowedMethods);
      }
    });

    // 移除没有任何操作的路径
    paths.entrySet().removeIf(entry ->
        entry.getValue() == null || entry.getValue().readOperations().isEmpty());
  }

  private Set<HttpMethod> getAllowedMethods() {
    return Arrays.stream(methodsToMatch.split(","))
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .map(method -> {
          try {
            return HttpMethod.valueOf(method.toUpperCase());
          } catch (IllegalArgumentException e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }

  private void filterMethods(PathItem pathItem, Set<HttpMethod> allowedMethods) {
    if (!allowedMethods.contains(HttpMethod.GET)) {
      pathItem.setGet(null);
    }
    if (!allowedMethods.contains(HttpMethod.POST)) {
      pathItem.setPost(null);
    }
    if (!allowedMethods.contains(HttpMethod.PUT)) {
      pathItem.setPut(null);
    }
    if (!allowedMethods.contains(HttpMethod.DELETE)) {
      pathItem.setDelete(null);
    }
    if (!allowedMethods.contains(HttpMethod.PATCH)) {
      pathItem.setPatch(null);
    }
    if (!allowedMethods.contains(HttpMethod.HEAD)) {
      pathItem.setHead(null);
    }
    if (!allowedMethods.contains(HttpMethod.OPTIONS)) {
      pathItem.setOptions(null);
    }
    if (!allowedMethods.contains(HttpMethod.TRACE)) {
      pathItem.setTrace(null);
    }
  }

  private void collectUsedTagsAndSchemas(OpenAPI openAPI, Set<String> usedTags, Set<String> usedSchemas) {
    // 收集所有使用的tags
    openAPI.getPaths().forEach((path, pathItem) -> {
      if (pathItem != null) {
        pathItem.readOperations().forEach(operation -> {
          if (operation.getTags() != null) {
            usedTags.addAll(operation.getTags());
          }
          // 收集请求体schema
          collectRequestSchemas(operation, usedSchemas);
          // 收集响应schema
          collectResponseSchemas(operation, usedSchemas);
        });
      }
    });
  }

  private void collectRequestSchemas(Operation operation, Set<String> usedSchemas) {
    if (operation.getRequestBody() != null
        && operation.getRequestBody().getContent() != null) {
      operation.getRequestBody().getContent().values().forEach(mediaType -> {
        if (mediaType.getSchema() != null) {
          collectSchemaReferences(mediaType.getSchema(), usedSchemas);
        }
      });
    }
  }

  private void collectResponseSchemas(Operation operation, Set<String> usedSchemas) {
    if (operation.getResponses() != null) {
      operation.getResponses().values().forEach(response -> {
        if (response.getContent() != null) {
          response.getContent().values().forEach(mediaType -> {
            if (mediaType.getSchema() != null) {
              collectSchemaReferences(mediaType.getSchema(), usedSchemas);
            }
          });
        }
      });
    }
  }

  private void collectSchemaReferences(Schema<?> schema, Set<String> usedSchemas) {
    if (schema.get$ref() != null) {
      // 从引用中提取schema名称
      String schemaName = schema.get$ref().replace("#/components/schemas/", "");
      usedSchemas.add(schemaName);
    }

    // 处理数组类型
    if (schema.getItems() != null) {
      collectSchemaReferences(schema.getItems(), usedSchemas);
    }

    // 处理属性中的schema
    if (schema.getProperties() != null) {
      schema.getProperties().values().forEach(propertySchema ->
          collectSchemaReferences((Schema<?>) propertySchema, usedSchemas));
    }
  }

  private void filterUnusedTags(OpenAPI openAPI, Set<String> usedTags) {
    if (openAPI.getTags() != null) {
      List<Tag> filteredTags = openAPI.getTags().stream()
          .filter(tag -> usedTags.contains(tag.getName()))
          .collect(Collectors.toList());
      openAPI.setTags(filteredTags);
    }
  }

  private void filterUnusedSchemas(OpenAPI openAPI, Set<String> usedSchemas) {
    if (openAPI.getComponents() != null
        && openAPI.getComponents().getSchemas() != null) {
      Map<String, Schema> schemas = openAPI.getComponents().getSchemas();
      schemas.keySet().removeIf(key -> !usedSchemas.contains(key));
    }
  }

}

