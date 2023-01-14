package edu.prokopchuk.springboottutorial.service;

import static org.junit.jupiter.api.Assertions.*;

import edu.prokopchuk.springboottutorial.model.Flight;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FlightServiceTest {
  private static FlightService flightService;

  @BeforeAll
  static void setUp() {
    flightService = new FlightService();
  }

  @Test
  void createFlightWorksProperly() {
    Flight actual = flightService.createFlight(new Flight());
    assertNotNull(actual);
  }

  @Test
  void getFlightWorksProperly() {
    Optional<Flight> actual = flightService.getFlight("test");
    assertTrue(actual.isPresent());
  }

  @Test
  void updateFlightWorksProperly() {
    Flight actual = flightService.updateFlight("test", new Flight());
    assertNotNull(actual);
  }

  @Test
  void deleteFlightWorksProperly() {
    boolean actual = flightService.deleteFlight("test");
    assertTrue(actual);
  }
}