package edu.prokopchuk.springboottutorial.service;

import static org.junit.jupiter.api.Assertions.*;

import edu.prokopchuk.springboottutorial.dao.UserDao;
import edu.prokopchuk.springboottutorial.model.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UserServiceTest {
  private static UserDao userDao;
  private static UserService userService;

  @BeforeAll
  static void setUp() {
    userDao = new UserDao();
    userService = new UserService(userDao);
  }

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