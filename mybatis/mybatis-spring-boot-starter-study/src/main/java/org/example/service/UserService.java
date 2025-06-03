package org.example.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.model.domain.User;
import org.example.model.request.UserQueryRequest;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.filter.QueryConditionBuilder;
import org.example.mybatis.query.operator.SQLOperator;
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
            newSimpleCondition("username", SQLOperator.LIKE, userQueryRequest.getUsername()),
            newSimpleCondition("firstName", SQLOperator.LIKE, userQueryRequest.getFirstName()),
            newSimpleCondition("lastName", SQLOperator.LIKE, userQueryRequest.getLastName())
        ).and(
            newSimpleCondition("isEnabled", SQLOperator.EQ, userQueryRequest.getIsEnabled()),
            newSimpleCondition("isDeleted", SQLOperator.EQ, userQueryRequest.getIsDeleted())
        ).build();
    List<String> fields = List.of("id", "username", "firstName", "lastName", "isEnabled", "isDeleted", "createAt", "updatedAt");
    return advancedSelect(user, conditions, userQueryRequest.getSorting(), fields);
  }

  public List<User> getUsers2(UserQueryRequest userQueryRequest) {
    final User user = new User();
    final List<Condition> conditions = QueryConditionBuilder.create()
        .or(
            newSimpleCondition("username", SQLOperator.LIKE, userQueryRequest.getUsername()),
            newSimpleCondition("firstName", SQLOperator.LIKE, userQueryRequest.getFirstName()),
            newSimpleCondition("lastName", SQLOperator.LIKE, userQueryRequest.getLastName())
        )
        .addCondition("isEnabled", SQLOperator.EQ, userQueryRequest.getIsEnabled())
        .addCondition("isDeleted", SQLOperator.EQ, userQueryRequest.getIsDeleted())
        .build();
    List<String> fields = List.of("id", "username", "firstName", "lastName", "isEnabled", "isDeleted", "createAt", "updatedAt");
    return advancedSelect(user, conditions, userQueryRequest.getSorting(), fields);
  }
}
