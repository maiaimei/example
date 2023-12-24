package cn.maiaimei.example.service;

import cn.maiaimei.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public String list() {
    return userRepository.list();
  }
}
