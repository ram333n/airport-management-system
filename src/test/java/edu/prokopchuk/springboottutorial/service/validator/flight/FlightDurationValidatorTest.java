package edu.prokopchuk.springboottutorial.service.validator.flight;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.model.Flight;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class FlightDurationValidatorTest {

  private final FlightDurationValidator validator = new FlightDurationValidator();

  @Test
  void supportsWhenProvidedUnsupportedClass() {
    assertFalse(validator.supports(CrewMember.class));
  }

  @Test
  void supportsWhenProvidedSupportedClass() {
    assertTrue(validator.supports(Flight.class));
  }

  @Test
  void validateWhenDepartureFromIsNull() {
    Flight flight = Flight.builder()
        .arrivalTime(LocalDateTime.now().plusHours(1L))
        .build();
    Errors errors = new BeanPropertyBindingResult(flight, "departureTime");

    validator.validate(flight, errors);

    assertTrue(errors.hasErrors());
    assertTrue(errors.hasFieldErrors("departureTime"));
  }

  @Test
  void validateWhenArrivalTimeFromIsNull() {
    Flight flight = Flight.builder()
        .departureTime(LocalDateTime.now().plusHours(1L))
        .build();
    Errors errors = new BeanPropertyBindingResult(flight, "arrivalTime");

    validator.validate(flight, errors);

    assertTrue(errors.hasErrors());
    assertTrue(errors.hasFieldErrors("arrivalTime"));
  }

  @Test
  void validateWhenProvidedWrongDuration() {
    Flight flight = Flight.builder()
        .departureTime(LocalDateTime.now().plusHours(2L))
        .arrivalTime(LocalDateTime.now().plusHours(1L))
        .build();
    Errors errors = new BeanPropertyBindingResult(flight, "departureTime");

    validator.validate(flight, errors);

    assertTrue(errors.hasErrors());
    assertTrue(errors.hasFieldErrors("departureTime"));
  }

  @Test
  void validateWhenProvidedValidDuration() {
    Flight flight = Flight.builder()
        .departureTime(LocalDateTime.now().plusHours(1L))
        .arrivalTime(LocalDateTime.now().plusHours(2L))
        .build();
    Errors errors = new BeanPropertyBindingResult(flight, "departureTime");

    validator.validate(flight, errors);

    assertFalse(errors.hasErrors());
  }

}