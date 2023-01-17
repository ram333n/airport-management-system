package edu.prokopchuk.springboottutorial.model;

import edu.prokopchuk.springboottutorial.model.enums.Position;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "crew_members")
public class CrewMember {
  @Id
  @Column(name = "pass_number")
  private String passNumber;

  @Column(name = "name")
  private String name;

  @Column(name = "surname")
  private String surname;

  @Column(name = "position")
  @Enumerated(value = EnumType.STRING)
  private Position position;

  //TODO: perform many-to-many mapping
  //private Set<Flight> flights;

}
