package edu.prokopchuk.springboottutorial.exception;

public class CrewMemberNotFoundException extends RuntimeException {

  public CrewMemberNotFoundException(String message) {
    super(message);
  }

}
