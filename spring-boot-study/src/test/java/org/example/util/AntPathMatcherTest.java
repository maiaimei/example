package org.example.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;

class AntPathMatcherTest {

  @Test
  @DisplayName("基本匹配示例")
  void demonstrateBasicMatching() {
    System.out.println("=== Basic Matching ===");

    // 单字符匹配
    matchAndPrint("com/t?st.jsp", "com/test.jsp");
    matchAndPrint("com/t?st.jsp", "com/tast.jsp");

    // 单层目录匹配
    matchAndPrint("com/*.jsp", "com/test.jsp");
    matchAndPrint("com/*.jsp", "com/sub/test.jsp");  // false

    // 多层目录匹配
    matchAndPrint("com/**/test.jsp", "com/test.jsp");
    matchAndPrint("com/**/test.jsp", "com/sub/test.jsp");
    matchAndPrint("com/**/test.jsp", "com/sub/sub2/test.jsp");
  }

  @Test
  @DisplayName("路径变量示例")
  void demonstratePathVariables() {
    AntPathMatcher pathMatcher = new AntPathMatcher();
    System.out.println("\n=== Path Variables ===");

    String pattern = "/users/{id}/profiles/{type}";
    String path = "/users/123/profiles/basic";

    if (pathMatcher.match(pattern, path)) {
      Map<String, String> variables = pathMatcher.extractUriTemplateVariables(pattern, path);

      System.out.println("Variables extracted:");
      variables.forEach((key, value) -> System.out.println(key + " = " + value));
    }
  }

  @Test
  @DisplayName("高级模式示例")
  void demonstrateAdvancedPatterns() {
    System.out.println("\n=== Advanced Patterns ===");

    // 组合模式
    matchAndPrint("**/META-INF/**/*.xml", "META-INF/spring/context.xml");

    // 多类型匹配
    matchAndPrint("/**/*.{jpg,png,gif}", "/images/photo.jpg");

    // 可选路径段
    matchAndPrint("/spring-?/{module}", "/spring-web/mvc");
  }

  @Test
  @DisplayName("自定义匹配示例")
  void demonstrateCustomization() {
    System.out.println("\n=== Custom Matching ===");

    AntPathMatcher customMatcher = new AntPathMatcher();
    customMatcher.setCaseSensitive(false);

    // 不区分大小写的匹配
    String pattern = "/Users/**/*.XML";
    String path = "/users/data/config.xml";

    System.out.println("Case-insensitive match: " + customMatcher.match(pattern, path));
  }

  // 辅助方法：执行匹配并打印结果
  void matchAndPrint(String pattern, String path) {
    AntPathMatcher pathMatcher = new AntPathMatcher();
    boolean matches = pathMatcher.match(pattern, path);
    System.out.printf("Pattern: %-20s Path: %-25s Matches: %b%n", pattern, path, matches);
  }

  @Test
  void pathMatchingComparison() {
    // Ant风格路径 - 语法简单直观
    String antPattern = "com/**/test/*.jsp";

    // 等效的正则表达式 - 语法较复杂
    String regexPattern = "com/.*?/test/[^/]+\\.jsp";

    // 示例路径
    String path = "com/a/b/test/sample.jsp";

    // Ant风格匹配
    AntPathMatcher antMatcher = new AntPathMatcher();
    boolean antMatch = antMatcher.match(antPattern, path);

    // 正则表达式匹配
    boolean regexMatch = path.matches(regexPattern);

    System.out.println("Ant Style Match: " + antMatch);
    System.out.println("Regex Match: " + regexMatch);
  }

  @Test
  @DisplayName("通配符对比")
  void demonstrateWildcards() {
    System.out.println("=== Wildcard Comparison ===");
    AntPathMatcher antMatcher = new AntPathMatcher();

    // Ant风格 - 简单通配符
    String antPattern = "/*.txt";

    // 正则表达式 - 更复杂的通配符
    String regexPattern = "/[^/]+\\.txt";

    String path = "/test.txt";
    System.out.println("Ant Style: " + antMatcher.match(antPattern, path));
    System.out.println("Regex: " + path.matches(regexPattern));
  }

  @Test
  @DisplayName("路径处理对比")
  void demonstratePathHandling() {
    System.out.println("\n=== Path Handling ===");
    AntPathMatcher antMatcher = new AntPathMatcher();

    // Ant风格 - 自动处理路径分隔符
    String antPattern = "/**/*.jsp";

    // 正则表达式 - 需要显式处理路径分隔符
    String regexPattern = ".+\\.jsp";

    String path = "path/to/file.jsp";
    System.out.println("Ant Style: " + antMatcher.match(antPattern, path));
    System.out.println("Regex: " + path.matches(regexPattern));
  }

  @Test
  @DisplayName("变量提取对比")
  void demonstrateExtractVariables() {
    System.out.println("\n=== Variable Extraction ===");
    AntPathMatcher antMatcher = new AntPathMatcher();

    // Ant风格 - 使用路径变量
    String antPattern = "/user/{id}/profile";

    // 正则表达式 - 使用捕获组
    String regexPattern = "/user/([^/]+)/profile";

    String path = "/user/123/profile";

    // Ant风格提取变量
    if (antMatcher.match(antPattern, path)) {
      Map<String, String> variables = antMatcher.extractUriTemplateVariables(antPattern, path);
      System.out.println("Ant Variables: " + variables);
    }

    // 正则表达式提取变量
    Pattern pattern = Pattern.compile(regexPattern);
    Matcher matcher = pattern.matcher(path);
    if (matcher.find()) {
      System.out.println("Regex Group: " + matcher.group(1));
    }
  }
}
