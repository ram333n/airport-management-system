package edu.prokopchuk.springboottutorial.service.validator.flight;

import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.repository.FlightRepository;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UniqueFlightNumberValidator implements Validator {

  private final FlightRepository flightRepository;

  @Autowired
  public UniqueFlightNumberValidator(FlightRepository flightRepository) {
    this.flightRepository = flightRepository;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Objects.equals(Flight.class, clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Flight flight = (Flight) target;
    String flightNumber = flight.getFlightNumber();

    if (flightRepository.existsById(flightNumber)) {
      errors.rejectValue("flightNumber",
          "flightNumberIsNotUnique",
          "Flight with this number already exists");
    }
  }

}
