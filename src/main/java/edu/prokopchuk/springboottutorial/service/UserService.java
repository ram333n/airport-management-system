package edu.prokopchuk.springboottutorial.service;

import edu.prokopchuk.springboottutorial.dao.UserDao;
import edu.prokopchuk.springboottutorial.model.User;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private UserDao userDao;

  @Autowired
  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  public User createUser(User user) {
    return new User();
  }

  public Optional<User> getUser(Long id) {
    return Optional.of(new User());
  }

  public User updateUser(Long id, User user) {
    return new User();
  }

  public boolean deleteUser(Long id) {
    return true;
  }
}
