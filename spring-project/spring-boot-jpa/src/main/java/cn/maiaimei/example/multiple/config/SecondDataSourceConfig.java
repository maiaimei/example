package cn.maiaimei.example.multiple.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
// 开启声明式事务
@EnableTransactionManagement
// 用来扫描指定的包及其子包中repository定义
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactorySecond",
        transactionManagerRef = "transactionManagerSecond",
        // 设置持久层所在位置
        basePackages = {"cn.maiaimei.example.multiple.jpa.ds2"})
public class SecondDataSourceConfig {
    @Autowired
    @Qualifier("secondDataSource")
    private DataSource secondDataSource;
    
    @Resource
    private Properties jpaProperties;

    @Bean(name = "entityManagerFactorySecond")
    public LocalContainerEntityManagerFactoryBean entityManagerFactorySecond() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.MYSQL);
        vendorAdapter.setShowSql(Boolean.TRUE);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        // 设置实体类包所在位置
        factory.setPackagesToScan("cn.maiaimei.example.multiple.jpa.ds2");
        // 指定数据源
        factory.setDataSource(secondDataSource);
        factory.setJpaProperties(jpaProperties);

        return factory;
    }

    @Bean(name = "transactionManagerSecond")
    public JpaTransactionManager transactionManagerSecond() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactorySecond().getObject());
        return transactionManager;
    }
}
