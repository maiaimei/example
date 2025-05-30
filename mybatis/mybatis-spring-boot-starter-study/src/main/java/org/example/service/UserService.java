package org.example.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  /**
   * 查询所有用户
   */
  public List<User> findAll() {
    return userRepository.findAll();
  }

  /**
   * 根据ID查询用户
   */
  public User findById(BigDecimal id) {
    return userRepository.findById(id);
  }

  /**
   * 创建新用户
   */
  public void createUser(User user) {
    user.setUserId(IdGenerator.nextId());
    user.setGmtCreate(LocalDateTime.now());
    user.setGmtModified(LocalDateTime.now());
    userRepository.insert(user);
  }

  /**
   * 更新用户信息
   */
  public void updateUser(User user) {
    user.setGmtModified(LocalDateTime.now());
    userRepository.update(user);
  }

  /**
   * 删除用户
   */
  public void deleteUser(BigDecimal id) {
    userRepository.deleteById(id);
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
      userRepository.batchInsert(batch);
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
    userRepository.batchUpdate(users);
  }

  /**
   * 批量更新用户（使用case when方式）
   */
  @Transactional
  public void batchUpdateUsersByCaseWhen(List<User> users, boolean updatePassword) {
    users.forEach(user -> {
      user.setGmtModified(LocalDateTime.now());
    });
    userRepository.batchUpdateByCaseWhen(users, updatePassword);
  }

}
