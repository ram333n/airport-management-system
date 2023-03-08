package edu.prokopchuk.springboottutorial.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "flights")
public class Flight {

  @Id
  @Column(name = "flight_number", length = 10)
  @NotBlank(message = "Flight number can not be blank")
  @Pattern(regexp = "\\S*", message = "Flight number can not contain any whitespace character")
  @Size(min = 3, message = "Flight number must contain at least 3 characters")
  @Size(max = 10, message = "Max length of flight number is 10 characters")
  private String flightNumber;

  @Column(name = "departure_from", nullable = false)
  @NotBlank(message = "Departure city can not be blank")
  @Size(min = 1, message = "Departure city must contain at least 1 character")
  @Size(max = 255, message = "Max length of departure city is 255 characters")
  private String departureFrom;

  @Column(name = "destination", nullable = false)
  @NotBlank(message = "Destination city can not be blank")
  @Size(min = 1, message = "Destination city must contain at least 1 character")
  @Size(max = 255, message = "Max length of destination is 255 characters")
  private String destination;

  @Column(name = "departure_time", nullable = false)
  @Future(message = "Departure time must be in future")
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime departureTime;

  @Column(name = "arrival_time", nullable = false)
  @Future(message = "Arrival time must be in future")
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime arrivalTime;

  @ManyToMany(mappedBy = "flights")
  private Set<CrewMember> crew = new HashSet<>();

  @PreRemove
  private void removeFlightFromCrewMembers() {
    for (CrewMember crewMember : crew) {
      crewMember.getFlights().remove(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Flight that = (Flight) o;

    return Objects.equals(flightNumber, that.flightNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(flightNumber);
  }

}
