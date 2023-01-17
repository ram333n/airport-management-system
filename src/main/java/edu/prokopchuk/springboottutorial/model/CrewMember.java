package edu.prokopchuk.springboottutorial.model;

import edu.prokopchuk.springboottutorial.model.enums.Position;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrewMember {
  private String passNumber;
  private String name;
  private String surname;
  private Position position;
  //TODO: perform many-to-many mapping
  //private Set<Flight> flights;

}
