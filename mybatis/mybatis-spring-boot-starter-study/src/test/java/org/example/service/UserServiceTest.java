package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.example.model.domain.User;
import org.example.model.request.UserQueryRequest;
import org.example.mybatis.query.filter.Condition;
import org.example.repository.core.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  /**
   * {@code @Captor} 是 Mockito 框架提供的一个注解，主要用于创建 ArgumentCaptor 实例，用来捕获方法调用时传入的参数值，以便后续进行断言验证。特别适合验证方法调用时的参数状态和值。
   */
  @Captor
  private ArgumentCaptor<User> userCaptor;

  @Captor
  private ArgumentCaptor<List<Condition>> conditionsCaptor;

  @Captor
  private ArgumentCaptor<List<String>> fieldsCaptor;

  private UserQueryRequest queryRequest;
  private List<User> userList;

  @BeforeEach
  void setUp() {
    // Initialize test data
    queryRequest = new UserQueryRequest();
    queryRequest.setUsername("testUser");
    queryRequest.setFirstName("John");
    queryRequest.setLastName("Doe");
    queryRequest.setIsEnabled(true);
    queryRequest.setIsDeleted(false);

    User testUser = new User();
    testUser.setUsername("testUser");
    testUser.setFirstName("John");
    testUser.setLastName("Doe");
    testUser.setIsEnabled(true);
    testUser.setIsDeleted(false);

    userList = new ArrayList<>();
    userList.add(testUser);
  }

  @Test
  void getUsers1_WithValidRequest_ShouldReturnUserList() {
    // Arrange
    when(userRepository.advancedSelect(any(User.class), anyList(), any(), any()))
        .thenReturn(userList);

    // Act
    List<User> result = userService.getUsers1(queryRequest);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("testUser", result.getFirst().getUsername());
    verify(userRepository, times(1))
        .advancedSelect(any(User.class), anyList(), any(), any());
  }

  @Test
  void getUsers2_WithValidRequest_ShouldReturnUserList() {
    // Arrange
    when(userRepository.advancedSelect(any(User.class), anyList(), any(), any()))
        .thenReturn(userList);

    // Act
    List<User> result = userService.getUsers2(queryRequest);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("testUser", result.getFirst().getUsername());
    verify(userRepository, times(1))
        .advancedSelect(any(User.class), anyList(), any(), any());
  }

  @Test
  void getUsers1_WithNullParameters_ShouldHandleGracefully() {
    // Arrange
    UserQueryRequest emptyRequest = new UserQueryRequest();
    when(userRepository.advancedSelect(any(User.class), anyList(), any(), any()))
        .thenReturn(new ArrayList<>());

    // Act
    List<User> result = userService.getUsers1(emptyRequest);

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void getUsers2_WithNullParameters_ShouldHandleGracefully() {
    // Arrange
    UserQueryRequest emptyRequest = new UserQueryRequest();
    when(userRepository.advancedSelect(any(User.class), anyList(), any(), any()))
        .thenReturn(new ArrayList<>());

    // Act
    List<User> result = userService.getUsers2(emptyRequest);

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void getUsers1_ShouldBuildCorrectConditions() {
    // Arrange
    when(userRepository.advancedSelect(any(User.class), any(), any(), any()))
        .thenReturn(userList);

    // Act
    userService.getUsers1(queryRequest);

    // Assert
    verify(userRepository).advancedSelect(
        userCaptor.capture(),
        conditionsCaptor.capture(),
        any(),
        fieldsCaptor.capture()
    );

    // Verify fields
    List<String> expectedFields = List.of(
        "id", "username", "firstName", "lastName",
        "isEnabled", "isDeleted", "createAt", "updatedAt"
    );
    List<String> actualFields = fieldsCaptor.getValue();
    assertNotNull(actualFields);
    assertEquals(expectedFields.size(), actualFields.size());
    assertTrue(actualFields.containsAll(expectedFields));

    // Verify conditions
    List<Condition> conditions = conditionsCaptor.getValue();
    assertNotNull(conditions);
    assertFalse(conditions.isEmpty());

    // Verify user
    User capturedUser = userCaptor.getValue();
    assertNotNull(capturedUser);
  }

  @Test
  void getUsers2_ShouldBuildCorrectConditions() {
    // Arrange
    when(userRepository.advancedSelect(any(User.class), any(), any(), any()))
        .thenReturn(userList);

    // Act
    userService.getUsers2(queryRequest);

    // Assert
    verify(userRepository).advancedSelect(
        userCaptor.capture(),
        conditionsCaptor.capture(),
        any(),
        fieldsCaptor.capture()
    );

    // Verify fields
    List<String> expectedFields = List.of(
        "id", "username", "firstName", "lastName",
        "isEnabled", "isDeleted", "createAt", "updatedAt"
    );
    List<String> actualFields = fieldsCaptor.getValue();
    assertNotNull(actualFields);
    assertEquals(expectedFields.size(), actualFields.size());
    assertTrue(actualFields.containsAll(expectedFields));

    // Verify conditions
    List<Condition> conditions = conditionsCaptor.getValue();
    assertNotNull(conditions);
    assertFalse(conditions.isEmpty());

    // Verify user
    User capturedUser = userCaptor.getValue();
    assertNotNull(capturedUser);
  }

  @Test
  void getUsers1_WhenRepositoryThrowsException_ShouldPropagateException() {
    // Arrange
    when(userRepository.advancedSelect(any(User.class), anyList(), any(), any()))
        .thenThrow(new RuntimeException("Database error"));

    // Act & Assert
    assertThrows(RuntimeException.class, () -> userService.getUsers1(queryRequest));
  }

  @Test
  void getUsers2_WhenRepositoryThrowsException_ShouldPropagateException() {
    // Arrange
    when(userRepository.advancedSelect(any(User.class), anyList(), any(), any()))
        .thenThrow(new RuntimeException("Database error"));

    // Act & Assert
    assertThrows(RuntimeException.class, () -> userService.getUsers2(queryRequest));
  }

  @Test
  void constructor_ShouldInitializeCorrectly() {
    // Arrange & Act
    UserService service = new UserService(userRepository);

    // Assert
    assertNotNull(service);
  }
}
