package edu.prokopchuk.springboottutorial.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

  @ManyToMany(
      mappedBy = "flights",
      cascade = CascadeType.REMOVE
  )
  private Set<CrewMember> crew = new HashSet<>();
}
