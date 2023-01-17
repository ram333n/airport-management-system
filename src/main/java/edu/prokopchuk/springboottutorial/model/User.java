package edu.prokopchuk.springboottutorial.model;

import edu.prokopchuk.springboottutorial.model.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
  private String email;
  private UserRole role;
}
