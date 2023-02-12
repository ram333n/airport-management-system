package edu.prokopchuk.springboottutorial.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "flights")
public class Flight {

  @Id
  @Column(name = "flight_number")
  private String flightNumber;

  @Column(
      name = "departure_from",
      nullable = false
  )
  private String departureFrom;

  @Column(
      name = "destination",
      nullable = false
  )
  private String destination;

  @Column(
      name = "departure_time",
      nullable = false
  )
  private LocalDateTime departureTime;

  @Column(
      name = "arrival_time",
      nullable = false
  )
  private LocalDateTime arrivalTime;

  @ManyToMany(mappedBy = "flights")
  private Set<CrewMember> crew = new HashSet<>();

  @PreRemove
  private void removeFlightsFromCrewMembers() {
    for (CrewMember crewMember : crew) {
      crewMember.getFlights().remove(this);
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

    Flight that = (Flight) o;
    return Objects.equals(flightNumber, that.flightNumber);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

}
