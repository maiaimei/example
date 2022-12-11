package cn.maiaimei.demo.tx.repository;

import cn.maiaimei.demo.tx.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insert(User user) {
        String sql = "insert into sys_user(id,nickname,username,password,is_enabled,is_deleted,gmt_create,gmt_modified) values (?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                user.getId(), user.getNickname(), user.getUsername(), user.getPassword(),
                user.getIsEnabled(), user.getIsDeleted(), user.getGmtCreate(), user.getGmtModified());
    }
}
