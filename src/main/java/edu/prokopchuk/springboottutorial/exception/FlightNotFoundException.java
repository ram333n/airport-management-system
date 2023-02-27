package edu.prokopchuk.springboottutorial.exception;

public class FlightNotFoundException extends RuntimeException {

  public FlightNotFoundException(String message) {
    super(message);
  }

}
