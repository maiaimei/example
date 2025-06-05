package org.example.service;

import static org.example.mybatis.query.filter.SimpleConditionFactory.eq;
import static org.example.mybatis.query.filter.SimpleConditionFactory.like;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.model.domain.User;
import org.example.model.request.UserQueryRequest;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.filter.QueryConditionBuilder;
import org.example.repository.core.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService extends AbstractDomainRepositoryService<User, UserRepository> {

  public UserService(UserRepository repository) {
    super(repository);
  }

  public List<User> getUsers1(UserQueryRequest userQueryRequest) {
    final User user = new User();
    final List<Condition> conditions = QueryConditionBuilder.create()
        .or(
            like("username", userQueryRequest.getUsername()),
            like("firstName", userQueryRequest.getFirstName()),
            like("lastName", userQueryRequest.getLastName())
        ).and(
            eq("isEnabled", userQueryRequest.getIsEnabled()),
            eq("isDeleted", userQueryRequest.getIsDeleted())
        ).build();
    List<String> fields = List.of("id", "username", "firstName", "lastName", "isEnabled", "isDeleted", "createAt", "updatedAt");
    return advancedSelect(user, conditions, fields, userQueryRequest.getSorting());
  }

  public List<User> getUsers2(UserQueryRequest userQueryRequest) {
    final User user = new User();
    final List<Condition> conditions = QueryConditionBuilder.create()
        .or(
            like("username", userQueryRequest.getUsername()),
            like("firstName", userQueryRequest.getFirstName()),
            like("lastName", userQueryRequest.getLastName())
        )
        .andEquals("isEnabled", userQueryRequest.getIsEnabled())
        .andEquals("isDeleted", userQueryRequest.getIsDeleted())
        .build();
    List<String> fields = List.of("id", "username", "firstName", "lastName", "isEnabled", "isDeleted", "createAt", "updatedAt");
    return advancedSelect(user, conditions, fields, userQueryRequest.getSorting());
  }
}
