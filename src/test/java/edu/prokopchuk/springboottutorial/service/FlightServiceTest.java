package edu.prokopchuk.springboottutorial.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.repository.FlightRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

  @Mock
  private static FlightRepository flightRepository;

  @InjectMocks
  private static FlightService flightService;

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