package cn.maiaimei.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import cn.maiaimei.example.model.FieldValueTestBean;
import cn.maiaimei.example.model.MovieRecommender;
import cn.maiaimei.example.model.PropertyValueTestBean;
import cn.maiaimei.example.model.SimpleMovieLister;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@Slf4j
@SpringJUnitConfig(value = TestConfig.class)
//@TestPropertySource(properties = {"user.region = CN"})
public class SpELInBeanDefinitionsTest {

  @Autowired
  private ApplicationContext applicationContext;

  @BeforeAll
  public static void beforeAll() {
    System.setProperty("user.region", "CN");
  }

  @Test
  public void test_field_value() {
    final FieldValueTestBean bean = applicationContext.getBean(FieldValueTestBean.class);
    assertNotNull(bean);
    final String defaultLocale = bean.getDefaultLocale();
    log.info("{}", defaultLocale);
    assertNotNull(defaultLocale);
  }

  @Test
  public void test_property_setter_method() {
    final PropertyValueTestBean bean = applicationContext.getBean(PropertyValueTestBean.class);
    assertNotNull(bean);
    final String defaultLocale = bean.getDefaultLocale();
    log.info("{}", defaultLocale);
    assertNotNull(defaultLocale);
  }

  @Test
  public void test_method_autowired() {
    final SimpleMovieLister bean = applicationContext.getBean(SimpleMovieLister.class);
    assertNotNull(bean);
    final String defaultLocale = bean.getDefaultLocale();
    log.info("{}", defaultLocale);
    assertNotNull(defaultLocale);
  }

  @Test
  public void test_constructor_autowired() {
    final MovieRecommender bean = applicationContext.getBean(MovieRecommender.class);
    assertNotNull(bean);
    final String defaultLocale = bean.getDefaultLocale();
    log.info("{}", defaultLocale);
    assertNotNull(defaultLocale);
  }
}
