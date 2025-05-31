package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.model.domain.User;
import org.example.repository.core.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService extends AbstractDomainRepositoryService<User, UserRepository> {

  public UserService(UserRepository repository) {
    super(repository);
  }
}
