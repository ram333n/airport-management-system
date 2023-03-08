package edu.prokopchuk.springboottutorial.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.model.Flight;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@AutoConfigureTestDatabase
@SqlGroup({
    @Sql(value = "classpath:clear.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "classpath:test-init.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
})
class CrewFlightsLinkServiceTest {

  @Autowired
  private CrewFlightsLinkService crewFlightsLinkService;

  @Autowired
  private CrewService crewService;

  @Autowired
  private FlightService flightService;

  @Test
  void contextLoads() {
    assertNotNull(crewFlightsLinkService);
    assertNotNull(crewService);
    assertNotNull(flightService);
  }

  @ParameterizedTest
  @CsvSource({
      "ABC-1, MAU-111", //provided non-existent passNumber
      "PLT-123, ABC-1", //provided non-existent flightNumber
      "ABC-1, ABC-1" //provided non-existent pass- and flight- numbers
  })
  void linkCrewMemberAndFlightReturnsFalse(String passNumber, String flightNumber) {
    boolean actual = crewFlightsLinkService.linkCrewMemberAndFlight(passNumber, flightNumber);

    assertFalse(actual);
  }

  @Test
  void linkCrewMemberAndFlightWhenProvidedAlreadyLinkedEntities() {
    String passNumber = "OPR-244";
    String flightNumber = "MAU-111";

    boolean actual = crewFlightsLinkService.linkCrewMemberAndFlight(passNumber, flightNumber);

    CrewMember crewMember = crewService.getCrewMember(passNumber).orElseThrow();
    Flight flight = flightService.getFlight(flightNumber).orElseThrow();

    List<CrewMember> crew = crewService.getCrewOfFlight(flight, null).toList(); //pass null to get all
    List<Flight> flights = flightService.getFlightsOfCrewMember(crewMember, null).toList();

    Optional<CrewMember> linkedMemberWithFlight = crew.stream()
        .filter(m -> Objects.equals(m.getPassNumber(), passNumber))
        .findAny();

    Optional<Flight> linkedFlightWithMember = flights.stream()
        .filter(f -> Objects.equals(f.getFlightNumber(), flightNumber))
        .findAny();

    assertTrue(actual);

    assertEquals(3, crew.size());
    assertEquals(2, flights.size());

    assertTrue(linkedMemberWithFlight.isPresent());
    assertEquals(passNumber, linkedMemberWithFlight.get().getPassNumber());

    assertTrue(linkedFlightWithMember.isPresent());
    assertEquals(flightNumber, linkedFlightWithMember.get().getFlightNumber());
  }

  @Test
  void linkCrewMemberAndFlightWorksProperly() {
    String passNumber = "OPR-244";
    String flightNumber = "TA-312";

    boolean actual = crewFlightsLinkService.linkCrewMemberAndFlight(passNumber, flightNumber);

    CrewMember crewMember = crewService.getCrewMember(passNumber).orElseThrow();
    Flight flight = flightService.getFlight(flightNumber).orElseThrow();

    List<CrewMember> crew = crewService.getCrewOfFlight(flight, null).toList(); //pass null to get all
    List<Flight> flights = flightService.getFlightsOfCrewMember(crewMember, null).toList();

    Optional<CrewMember> linkedMemberWithFlight = crew.stream()
            .filter(m -> Objects.equals(m.getPassNumber(), passNumber))
            .findAny();

    Optional<Flight> linkedFlightWithMember = flights.stream()
            .filter(f -> Objects.equals(f.getFlightNumber(), flightNumber))
            .findAny();

    assertTrue(actual);

    assertEquals(3, crew.size());
    assertEquals(3, flights.size());

    assertTrue(linkedMemberWithFlight.isPresent());
    assertEquals(passNumber, linkedMemberWithFlight.get().getPassNumber());

    assertTrue(linkedFlightWithMember.isPresent());
    assertEquals(flightNumber, linkedFlightWithMember.get().getFlightNumber());
  }

  @ParameterizedTest
  @CsvSource({
      "ABC-1, MAU-111", //provided non-existent passNumber
      "PLT-123, ABC-1", //provided non-existent flightNumber
      "ABC-1, ABC-1" //provided non-existent pass- and flight- numbers
  })
  void unlinkCrewMemberAndFlightWhenSomeEntityIsNonExistent(String passNumber, String flightNumber) {
    boolean actual = crewFlightsLinkService.unlinkCrewMemberAndFlight(passNumber, flightNumber);

    assertFalse(actual);
  }

  @Test
  void unlinkCrewMemberAndFlightWhenLinkNotExists() {
    String passNumber = "OPR-244";
    String flightNumber = "TA-312";

    boolean actual = crewFlightsLinkService.unlinkCrewMemberAndFlight(passNumber, flightNumber);

    CrewMember crewMember = crewService.getCrewMember(passNumber).orElseThrow();
    Flight flight = flightService.getFlight(flightNumber).orElseThrow();

    List<CrewMember> crew = crewService.getCrewOfFlight(flight, null).toList(); //pass null to get all
    List<Flight> flights = flightService.getFlightsOfCrewMember(crewMember, null).toList();

    Optional<CrewMember> linkedMemberWithFlight = crew.stream()
        .filter(m -> Objects.equals(m.getPassNumber(), passNumber))
        .findAny();

    Optional<Flight> linkedFlightWithMember = flights.stream()
        .filter(f -> Objects.equals(f.getFlightNumber(), flightNumber))
        .findAny();

    assertFalse(actual);

    assertEquals(2, crew.size());
    assertEquals(2, flights.size());

    assertTrue(linkedMemberWithFlight.isEmpty());
    assertTrue(linkedFlightWithMember.isEmpty());
  }

  @Test
  void unlinkCrewMemberAndFlightWorksProperly() {
    String passNumber = "OPR-244";
    String flightNumber = "MAU-111";

    boolean actual = crewFlightsLinkService.unlinkCrewMemberAndFlight(passNumber, flightNumber);

    CrewMember crewMember = crewService.getCrewMember(passNumber).orElseThrow();
    Flight flight = flightService.getFlight(flightNumber).orElseThrow();

    List<CrewMember> crew = crewService.getCrewOfFlight(flight, null).toList(); //pass null to get all
    List<Flight> flights = flightService.getFlightsOfCrewMember(crewMember, null).toList();

    Optional<CrewMember> linkedMemberWithFlight = crew.stream()
        .filter(m -> Objects.equals(m.getPassNumber(), passNumber))
        .findAny();

    Optional<Flight> linkedFlightWithMember = flights.stream()
        .filter(f -> Objects.equals(f.getFlightNumber(), flightNumber))
        .findAny();

    assertTrue(actual);

    assertEquals(2, crew.size());
    assertEquals(1, flights.size());

    assertTrue(linkedMemberWithFlight.isEmpty());
    assertTrue(linkedFlightWithMember.isEmpty());
  }

}