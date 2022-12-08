package cn.maiaimei.demo.ioc.component;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class DbProperties {
    @Value("${db.mysql.url}")
    private String url;

    @Value("${db.mysql.username}")
    private String username;

    @Value("${db.mysql.password}")
    private String password;

    @Override
    public String toString() {
        return "DbProperties{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
