package edu.prokopchuk.springboottutorial.service.validator.flight;

import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.repository.FlightRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;

@Service
public class FlightValidator {

  private final FlightRepository flightRepository;

  @Autowired
  public FlightValidator(FlightRepository flightRepository) {
    this.flightRepository = flightRepository;
  }

  public void validateFlightNumberUniqueness(Flight flight, Errors errors) {
    String flightNumber = flight.getFlightNumber();

    if (flightRepository.existsById(flightNumber)) {
      errors.rejectValue("flightNumber",
          "flightNumberIsNotUnique",
          "Flight with this number already exists");
    }
  }

  public void validateDurationBoundaries(Flight flight, Errors errors) {
    LocalDateTime departureTime = flight.getDepartureTime();
    LocalDateTime arrivalTime = flight.getArrivalTime();

    ValidationUtils.rejectIfEmpty(
        errors,
        "departureTime",
        "departureTimeIsNull",
        "Departure time can not be empty"
    );

    ValidationUtils.rejectIfEmpty(
        errors,
        "arrivalTime",
        "arrivalTimeIsNull",
        "Arrival time can not be empty"
    );

    List<FieldError> departureTimeErrors = errors.getFieldErrors("departureTime");
    List<FieldError> arrivalTimeErrors = errors.getFieldErrors("arrivalTime");

    boolean isDepartureTimeNull = containsError(departureTimeErrors, "departureTimeIsNull");
    boolean isArrivalTimeNull = containsError(arrivalTimeErrors, "arrivalTimeIsNull");

    if (isDepartureTimeNull || isArrivalTimeNull) {
      return;
    }

    if (departureTime.isAfter(arrivalTime)) {
      errors.rejectValue("departureTime",
          "departureTimeIsBeforeArrivalTime",
          "Departure time must be before arrival time");
    }
  }

  private boolean containsError(List<FieldError> fieldErrors, String errorCode) {
    for (FieldError error : fieldErrors) {
      if (Objects.equals(error.getCode(), errorCode)) {
        return true;
      }
    }

    return false;
  }

}
