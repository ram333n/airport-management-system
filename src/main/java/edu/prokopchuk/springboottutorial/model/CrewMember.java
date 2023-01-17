package edu.prokopchuk.springboottutorial.model;

import edu.prokopchuk.springboottutorial.model.enums.Position;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
