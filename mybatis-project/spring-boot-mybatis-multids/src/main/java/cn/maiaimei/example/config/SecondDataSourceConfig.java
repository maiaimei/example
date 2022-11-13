package cn.maiaimei.example.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackages = "cn.maiaimei.example.mapper.second",
        sqlSessionFactoryRef = "sqlSessionFactorySecond",
        sqlSessionTemplateRef = "sqlSessionTemplateSecond"
)
public class SecondDataSourceConfig {
    @Autowired
    @Qualifier("secondDataSource")
    private DataSource secondDataSource;

    @Bean("sqlSessionFactorySecond")
    public SqlSessionFactory sqlSessionFactorySecond() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(secondDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:cn/maiaimei/example/mapper/second/*.xml"));
        return bean.getObject();
    }

    @Bean("sqlSessionTemplateSecond")
    public SqlSessionTemplate sqlSessionTemplateSecond(@Qualifier("sqlSessionFactorySecond") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean("transactionManagerSecond")
    public DataSourceTransactionManager transactionManagerSecond() {
        return new DataSourceTransactionManager(secondDataSource);
    }
}
