package edu.prokopchuk.springboottutorial.service.validator.flight;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.repository.FlightRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@ExtendWith(MockitoExtension.class)
class FlightValidatorTest {

  @Mock
  private FlightRepository flightRepository;

  @InjectMocks
  private FlightValidator validator;

  @Test
  void validateFlightNumberUniquenessWhenProvidedExistentFlightNumber() {
    String flightNumber = "TEST-1";
    Flight flight = Flight.builder()
        .flightNumber(flightNumber)
        .build();
    Errors errors = new BeanPropertyBindingResult(flight, "flightNumber");

    Mockito.when(flightRepository.existsById(flightNumber)).thenReturn(true);

    validator.validateFlightNumberUniqueness(flight, errors);

    assertTrue(errors.hasErrors());
    assertTrue(errors.hasFieldErrors("flightNumber"));
  }

  @Test
  void validateFlightNumberUniquenessWhenProvidedNonExistentFlightNumber() {
    String flightNumber = "TEST-1";
    Flight flight = Flight.builder()
        .flightNumber(flightNumber)
        .build();
    Errors errors = new BeanPropertyBindingResult(flight, "flightNumber");

    Mockito.when(flightRepository.existsById(flightNumber)).thenReturn(false);

    validator.validateFlightNumberUniqueness(flight, errors);

    assertFalse(errors.hasErrors());
  }

  @Test
  void validateDurationBoundariesWhenDepartureFromIsNull() {
    Flight flight = Flight.builder()
        .arrivalTime(LocalDateTime.now().plusHours(1L))
        .build();
    Errors errors = new BeanPropertyBindingResult(flight, "departureTime");

    validator.validateDurationBoundaries(flight, errors);

    assertTrue(errors.hasErrors());
    assertTrue(errors.hasFieldErrors("departureTime"));
  }

  @Test
  void validateDurationBoundariesWhenArrivalTimeFromIsNull() {
    Flight flight = Flight.builder()
        .departureTime(LocalDateTime.now().plusHours(1L))
        .build();
    Errors errors = new BeanPropertyBindingResult(flight, "arrivalTime");

    validator.validateDurationBoundaries(flight, errors);

    assertTrue(errors.hasErrors());
    assertTrue(errors.hasFieldErrors("arrivalTime"));
  }

  @Test
  void validateDurationBoundariesWhenProvidedWrongDuration() {
    Flight flight = Flight.builder()
        .departureTime(LocalDateTime.now().plusHours(2L))
        .arrivalTime(LocalDateTime.now().plusHours(1L))
        .build();
    Errors errors = new BeanPropertyBindingResult(flight, "departureTime");

    validator.validateDurationBoundaries(flight, errors);

    assertTrue(errors.hasErrors());
    assertTrue(errors.hasFieldErrors("departureTime"));
  }

  @Test
  void validateDurationBoundariesWhenProvidedValidDuration() {
    Flight flight = Flight.builder()
        .departureTime(LocalDateTime.now().plusHours(1L))
        .arrivalTime(LocalDateTime.now().plusHours(2L))
        .build();
    Errors errors = new BeanPropertyBindingResult(flight, "departureTime");

    validator.validateDurationBoundaries(flight, errors);

    assertFalse(errors.hasErrors());
  }

}