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
        basePackages = "cn.maiaimei.example.mapper.first",
        sqlSessionFactoryRef = "sqlSessionFactoryFirst",
        sqlSessionTemplateRef = "sqlSessionTemplateFirst"
)
public class FirstDataSourceConfig {
    @Autowired
    @Qualifier("firstDataSource")
    private DataSource firstDataSource;

    @Bean("sqlSessionFactoryFirst")
    public SqlSessionFactory sqlSessionFactoryFirst() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(firstDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:cn/maiaimei/example/mapper/first/*.xml"));
        return bean.getObject();
    }

    @Bean("sqlSessionTemplateFirst")
    public SqlSessionTemplate sqlSessionTemplateFirst(@Qualifier("sqlSessionFactoryFirst") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean("transactionManagerFirst")
    public DataSourceTransactionManager transactionManagerFirst() {
        return new DataSourceTransactionManager(firstDataSource);
    }
}
