package edu.prokopchuk.springboottutorial.model;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Flight {
  private String flightNumber;
  private String departureFrom;
  private String destination;
  private LocalDateTime departureTime;
  private LocalDateTime arrivalTime;

  //TODO: perform many-to-many mapping
  //private Set<CrewMember> crew;
}
