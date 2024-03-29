package cn.maiaimei.example.registrar;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class OperationFactoryBean implements FactoryBean<Object>, InitializingBean {

  private Class<?> type;

  private String expression;

  private Expression spelExpression;

  @Override
  public Object getObject() throws Exception {
    return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type},
        new InvocationHandler() {
          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            final StandardEvaluationContext context = new StandardEvaluationContext();
            // 第一个参数：a，第二个参数：b，与cn.maiaimei.example.registrar.Operation.value参数占位符一一对应
            char argName = 'a';
            for (Object arg : args) {
              context.setVariable(String.valueOf(argName++), arg);
            }
            // 通过SpEL表达式计算结果
            return spelExpression.getValue(context);
          }
        });
  }

  @Override
  public Class<?> getObjectType() {
    return type;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    final SpelExpressionParser parser = new SpelExpressionParser();
    this.spelExpression = parser.parseExpression(this.expression);
  }

  public Class<?> getType() {
    return type;
  }

  public void setType(Class<?> type) {
    this.type = type;
  }

  public String getExpression() {
    return expression;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }
}
