package edu.prokopchuk.springboottutorial.service.validator.flight;

import static org.junit.jupiter.api.Assertions.*;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@ExtendWith(MockitoExtension.class)
class UniqueFlightNumberValidatorTest {

  @Mock
  private FlightRepository flightRepository;

  @InjectMocks
  private UniqueFlightNumberValidator validator;

  @Test
  void supportsWhenProvidedUnsupportedClass() {
    assertFalse(validator.supports(CrewMember.class));
  }

  @Test
  void supportsWhenProvidedSupportedClass() {
    assertTrue(validator.supports(Flight.class));
  }

  @Test
  void validateWhenProvidedExistentFlightNumber() {
    String flightNumber = "TEST-1";
    Flight flight = Flight.builder()
        .flightNumber(flightNumber)
        .build();
    Errors errors = new BeanPropertyBindingResult(flight, "flightNumber");

    Mockito.when(flightRepository.existsById(flightNumber)).thenReturn(true);

    validator.validate(flight, errors);

    assertTrue(errors.hasErrors());
    assertTrue(errors.hasFieldErrors("flightNumber"));
  }

  @Test
  void validateWhenProvidedNonExistentFlightNumber() {
    String flightNumber = "TEST-1";
    Flight flight = Flight.builder()
        .flightNumber(flightNumber)
        .build();
    Errors errors = new BeanPropertyBindingResult(flight, "flightNumber");

    Mockito.when(flightRepository.existsById(flightNumber)).thenReturn(false);

    validator.validate(flight, errors);

    assertFalse(errors.hasErrors());
  }

}