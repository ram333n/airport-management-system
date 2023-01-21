package edu.prokopchuk.springboottutorial.model;

import edu.prokopchuk.springboottutorial.model.enums.Position;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "crew_members")
public class CrewMember {
  @Id
  @Column(name = "pass_number")
  private String passNumber;

  @Column(
      name = "name",
      nullable = false
  )
  private String name;

  @Column(
      name = "surname",
      nullable = false
  )
  private String surname;

  @Column(
      name = "position",
      nullable = false
  )
  @Enumerated(value = EnumType.STRING)
  private Position position;

  @ManyToMany
  @JoinTable(
      name = "flights_crew",
      joinColumns = @JoinColumn(name = "pass_number"),
      inverseJoinColumns = @JoinColumn(name = "flight_number")
  )
  private Set<Flight> flights = new HashSet<>();

  @PreRemove
  private void removeFlights() {
    for (Flight flight : flights) {
      flight.getCrew().remove(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }

    CrewMember that = (CrewMember) o;
    return Objects.equals(passNumber, that.passNumber);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
