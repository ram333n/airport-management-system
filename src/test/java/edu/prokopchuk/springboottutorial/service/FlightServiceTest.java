package edu.prokopchuk.springboottutorial.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.repository.CrewRepository;
import edu.prokopchuk.springboottutorial.repository.FlightRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@AutoConfigureTestDatabase
@SqlGroup({
    @Sql(scripts = "classpath:clear.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(scripts = "classpath:test-init.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
})
class FlightServiceTest {

  @Autowired
  private FlightRepository flightRepository;

  @Autowired
  private CrewRepository crewRepository;

  @Autowired
  private FlightService flightService;

  @Test
  void contextLoads() {
    assertNotNull(flightRepository);
    assertNotNull(crewRepository);
    assertNotNull(flightService);
  }

  @Test
  void createFlightWorksProperly() {
    String flightNumber = "TEST-132";
    Flight flight = Flight.builder()
        .flightNumber(flightNumber)
        .departureFrom("Lviv")
        .destination("Rzeszow")
        .departureTime(LocalDateTime.now().plusHours(1L))
        .arrivalTime(LocalDateTime.now().plusHours(25L))
        .build();

    flightService.createFlight(flight);
    Optional<Flight> actual = flightService.getFlight(flightNumber);

    assertTrue(actual.isPresent());
    assertEquals(flightNumber, actual.get().getFlightNumber());
  }

  @Test
  void getFlightWorksProperly() {
    Optional<Flight> actual1 = flightService.getFlight("TEST-1");
    Optional<Flight> actual2 = flightService.getFlight("MAU-111");

    assertTrue(actual1.isEmpty());
    assertTrue(actual2.isPresent());
    assertEquals("Kyiv", actual2.get().getDepartureFrom());
  }

  @Test
  void getFlightsPageWorksProperly() {
    int pageNumber = 0;
    int pageSize = 2;
    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("destination"));

    Page<Flight> page = flightService.getFlightPage(pageable);
    List<Flight> flights = page.toList();

    assertEquals(2, flights.size());
    assertEquals("Berlin", flights.get(0).getDestination());
    assertEquals("Krakow", flights.get(1).getDestination());
  }

  @Test
  void getFlightsOfCrewMemberWorksProperly() {
    int pageNumber = 0;
    int pageSize = 3;
    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("destination"));

    Optional<CrewMember> crewMemberOptional = crewRepository.findById("PLT-123");
    CrewMember crewMember = crewMemberOptional.orElseThrow();

    Page<Flight> page = flightService.getFlightsOfCrewMember(crewMember, pageable);
    List<Flight> flights = page.toList();

    assertEquals(2, flights.size());
    assertEquals("Berlin", flights.get(0).getDestination());
    assertEquals("Krakow", flights.get(1).getDestination());
  }

  @Test
  void updateFlightWorksProperly() {
    String flightNumber = "MAU-111";
    Flight flight = Flight.builder()
        .flightNumber(flightNumber)
        .departureFrom("Lviv")
        .destination("Rzeszow")
        .departureTime(LocalDateTime.now().plusHours(1L))
        .arrivalTime(LocalDateTime.now().plusHours(25L))
        .build();

    flightService.updateFlight(flight);
    Optional<Flight> actual = flightService.getFlight(flightNumber);

    assertTrue(actual.isPresent());
    assertEquals("Lviv", actual.get().getDepartureFrom());
    assertEquals("Rzeszow", actual.get().getDestination());
  }

  @Test
  void deleteFlightWorksProperly() {
    String realFlightNumber = "MAU-955";
    String fakeFlightNumber = "TEST-1";

    boolean isRealDeleted = flightService.deleteFlight(realFlightNumber);
    boolean isFakeDeleted = flightService.deleteFlight(fakeFlightNumber);

    assertTrue(isRealDeleted);
    assertFalse(isFakeDeleted);
  }

}