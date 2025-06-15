package org.example.mybatis.interceptor;

import java.sql.PreparedStatement;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.example.mybatis.annotation.TypeHandler;
import org.example.mybatis.type.CustomTypeHandlerRegistry;
import org.springframework.util.ReflectionUtils;

@Intercepts({
    @Signature(type = ParameterHandler.class, method = "setParameters",
        args = {PreparedStatement.class})
})
@Slf4j
public class TypeHandlerInterceptor implements Interceptor {

  private final CustomTypeHandlerRegistry customTypeHandlerRegistry;

  public TypeHandlerInterceptor(CustomTypeHandlerRegistry customTypeHandlerRegistry) {
    this.customTypeHandlerRegistry = customTypeHandlerRegistry;
  }

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
    MetaObject metaObject = SystemMetaObject.forObject(parameterHandler);
    Object parameterObject = metaObject.getValue("parameterObject");

    if (parameterObject != null) {
      Class<?> parameterClass = parameterObject.getClass();
      // 处理参数对象中的TypeHandler注解
      processTypeHandlers(parameterClass, parameterObject);
    }

    return invocation.proceed();
  }

  private void processTypeHandlers(Class<?> parameterClass, Object parameterObject) {
    ReflectionUtils.doWithFields(parameterClass, field -> {
      TypeHandler annotation = field.getAnnotation(TypeHandler.class);
      if (annotation != null) {
        field.setAccessible(true);
        Object value = field.get(parameterObject);
        if (value != null) {
          // 获取并应用TypeHandler
          Class<?> handlerClass = customTypeHandlerRegistry.getTypeHandler(
              parameterClass, field.getName());
          if (handlerClass != null) {
            // 在这里处理类型转换
            log.debug("Applying type handler {} for field {}",
                handlerClass.getName(), field.getName());
          }
        }
      }
    });
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {
  }
}
