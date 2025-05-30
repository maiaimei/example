package org.example.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.datasource.DataSource;
import org.example.datasource.DataSourceContextHolder;
import org.example.model.domain.User;
import org.example.mybatis.SQLOperator;
import org.example.mybatis.query.BaseQuery;
import org.example.repository.UserRepository;
import org.example.repository.advanced.AdvancedUserRepository;
import org.example.repository.master.MasterUserRepository;
import org.example.repository.simple.SimpleUserRepository;
import org.example.repository.slave1.Slave1UserRepository;
import org.example.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

  @Autowired(required = false)
  private SimpleUserRepository simpleUserRepository;

  @Autowired(required = false)
  private MasterUserRepository masterUserRepository;

  @Autowired(required = false)
  private Slave1UserRepository slave1UserRepository;

  @Autowired
  private AdvancedUserRepository advancedUserRepository;

  private UserRepository getBasicUserRepository() {
    final String databaseName = Optional.ofNullable(DataSourceContextHolder.getDataSourceName()).orElse("");
    return switch (databaseName) {
      case "master" -> {
        log.info("Using MasterUserRepository");
        yield masterUserRepository;
      }
      case "slave1" -> {
        log.info("Using Slave1UserRepository");
        yield slave1UserRepository;
      }
      default -> {
        if (Objects.isNull(simpleUserRepository)) {
          log.info("Using MasterUserRepository");
          yield masterUserRepository;
        } else {
          log.info("Using UserRepository");
          yield simpleUserRepository;
        }
      }
    };
  }

  public List<User> searchUsers() {
    // 添加查询条件
    BaseQuery query = new BaseQuery();
    query.addCondition("username", SQLOperator.LIKE, "maiam");

    // 添加排序
    query.setSortField("gmt_create");
    query.setSortOrder("DESC");

    // 设置分页
    query.setPageNum(1);
    query.setPageSize(2);

    return advancedUserRepository.selectByConditions(new User(), query);
  }

  /**
   * 查询所有用户
   */
  @DataSource("slave1")
  public List<User> findAll() {
    return getBasicUserRepository().findAll();
  }

  /**
   * 根据ID查询用户
   */
  public User findById(BigDecimal id) {
    return getBasicUserRepository().findById(id);
  }

  /**
   * 创建新用户
   */
  public void createUser(User user) {
    user.setUserId(IdGenerator.nextId());
    user.setGmtCreate(LocalDateTime.now());
    user.setGmtModified(LocalDateTime.now());
    getBasicUserRepository().insert(user);
  }

  /**
   * 更新用户信息
   */
  public void updateUser(User user) {
    user.setGmtModified(LocalDateTime.now());
    getBasicUserRepository().update(user);
  }

  /**
   * 删除用户
   */
  public void deleteUser(BigDecimal id) {
    getBasicUserRepository().deleteById(id);
  }

  /**
   * 批量创建用户
   */
  @Transactional
  public void batchInsertUsers(List<User> users, int batchSize) {
    users.forEach(user -> {
      user.setUserId(IdGenerator.nextId());
      user.setGmtCreate(LocalDateTime.now());
      user.setGmtModified(LocalDateTime.now());
    });
    int total = users.size();
    for (int i = 0; i < total; i += batchSize) {
      int end = Math.min(i + batchSize, total);
      List<User> batch = users.subList(i, end);
      getBasicUserRepository().batchInsert(batch);
    }
  }

  /**
   * 批量更新用户（使用forEach方式）
   */
  @Transactional
  public void batchUpdateUsers(List<User> users) {
    users.forEach(user -> {
      user.setGmtModified(LocalDateTime.now());
    });
    getBasicUserRepository().batchUpdate(users);
  }

  /**
   * 批量更新用户（使用case when方式）
   */
  @Transactional
  public void batchUpdateUsersByCaseWhen(List<User> users, boolean updatePassword) {
    users.forEach(user -> {
      user.setGmtModified(LocalDateTime.now());
    });
    getBasicUserRepository().batchUpdateByCaseWhen(users, updatePassword);
  }

}
