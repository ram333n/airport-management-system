package edu.prokopchuk.springboottutorial.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.prokopchuk.springboottutorial.model.User;
import edu.prokopchuk.springboottutorial.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock
  private static UserRepository userRepository;

  @InjectMocks
  private static UserService userService;

  @Test
  void createUserWorksProperly() {
    User actual = userService.createUser(new User());
    assertNotNull(actual);
  }

  @Test
  void getUserWorksProperly() {
    Optional<User> actual = userService.getUser(0L);
    assertTrue(actual.isPresent());
  }

  @Test
  void updateUserWorksProperly() {
    User actual = userService.updateUser(0L, new User());
    assertNotNull(actual);
  }

  @Test
  void deleteUserWorksProperly() {
    boolean actual = userService.deleteUser(0L);
    assertTrue(actual);
  }
}