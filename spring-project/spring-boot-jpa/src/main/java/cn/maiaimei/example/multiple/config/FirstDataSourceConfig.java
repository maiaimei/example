package cn.maiaimei.example.multiple.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        entityManagerFactoryRef = "entityManagerFactoryFirst",
        transactionManagerRef = "transactionManagerFirst",
        // 设置持久层所在位置
        basePackages = {"cn.maiaimei.example.multiple.jpa.ds1"})
public class FirstDataSourceConfig {

    @Autowired
    @Qualifier("firstDataSource")
    private DataSource firstDataSource;

    @Resource
    private Properties jpaProperties;

    @Primary
    @Bean(name = "entityManagerFactoryFirst")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryFirst() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.MYSQL);
        vendorAdapter.setShowSql(Boolean.TRUE);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        // 设置实体类包所在位置
        factory.setPackagesToScan("cn.maiaimei.example.multiple.jpa.ds1");
        // 指定数据源
        factory.setDataSource(firstDataSource);
        factory.setJpaProperties(jpaProperties);

        return factory;
    }

    @Primary
    @Bean(name = "transactionManagerFirst")
    public JpaTransactionManager transactionManagerFirst() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactoryFirst().getObject());
        return transactionManager;
    }

}
