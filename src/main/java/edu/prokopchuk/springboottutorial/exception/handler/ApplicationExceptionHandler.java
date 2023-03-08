package edu.prokopchuk.springboottutorial.exception.handler;

import edu.prokopchuk.springboottutorial.exception.CrewMemberNotFoundException;
import edu.prokopchuk.springboottutorial.exception.FlightNotFoundException;
import edu.prokopchuk.springboottutorial.exception.NoSuchCrewMemberForFlightException;
import edu.prokopchuk.springboottutorial.exception.NoSuchFlightForCrewMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ApplicationExceptionHandler {

  @ExceptionHandler(CrewMemberNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelAndView crewMemberNotFound() {
    return new ModelAndView("crew-member-not-found");
  }

  @ExceptionHandler(FlightNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelAndView flightNotFound() {
    return new ModelAndView("flight-not-found");
  }

  @ExceptionHandler(NoSuchCrewMemberForFlightException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelAndView noSuchCrewMemberForFlightException() {
    return new ModelAndView("no-such-crew-member-for-flight");
  }

  @ExceptionHandler(NoSuchFlightForCrewMemberException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelAndView noSuchFlightForCrewMemberException() {
    return new ModelAndView("no-such-flight-for-crew-member");
  }

}
