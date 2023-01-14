package edu.prokopchuk.springboottutorial.service;

import edu.prokopchuk.springboottutorial.model.User;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  //TODO: add repository

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
