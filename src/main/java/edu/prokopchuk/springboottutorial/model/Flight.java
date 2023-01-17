package edu.prokopchuk.springboottutorial.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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

  @Column(name = "departure_from")
  private String departureFrom;

  @Column(name = "destination")
  private String destination;

  @Column(name = "departure_time")
  private LocalDateTime departureTime;

  @Column(name = "arrival_time")
  private LocalDateTime arrivalTime;

  //TODO: perform many-to-many mapping
  //private Set<CrewMember> crew;
}
