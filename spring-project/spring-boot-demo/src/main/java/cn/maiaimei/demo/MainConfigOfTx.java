package cn.maiaimei.demo;

import cn.maiaimei.framework.util.SFID;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@EnableTransactionManagement
@Configuration
@ComponentScan(basePackages = {
        "cn.maiaimei.demo.tx"
})
@Import({SFID.class})
@PropertySource("classpath:/db.properties")
public class MainConfigOfTx {
    private static final Logger log = LoggerFactory.getLogger(MainConfigOfTx.class);

    @Value("${db.h2.driverClassName}")
    private String driverClassName;
    @Value("${db.h2.url}")
    private String url;
    @Value("${db.h2.username}")
    private String username;
    @Value("${db.h2.password}")
    private String password;

    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        log.info("create dataSource");
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(driverClassName);
        dataSource.setJdbcUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() throws PropertyVetoException {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public TransactionManager transactionManager() throws PropertyVetoException {
        return new DataSourceTransactionManager(dataSource());
    }
}
