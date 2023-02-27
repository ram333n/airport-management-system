package edu.prokopchuk.springboottutorial.service.validator.flight;

import edu.prokopchuk.springboottutorial.model.Flight;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class FlightDurationValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Objects.equals(Flight.class, clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Flight flight = (Flight) target;
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
