package edu.prokopchuk.springboottutorial.exception;

public class NoSuchFlightForCrewMemberException extends RuntimeException {

  public NoSuchFlightForCrewMemberException(String message) {
    super(message);
  }

}
