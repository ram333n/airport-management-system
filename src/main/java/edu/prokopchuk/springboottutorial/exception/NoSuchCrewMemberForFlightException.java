package edu.prokopchuk.springboottutorial.exception;

public class NoSuchCrewMemberForFlightException extends RuntimeException {

  public NoSuchCrewMemberForFlightException(String message) {
    super(message);
  }

}
