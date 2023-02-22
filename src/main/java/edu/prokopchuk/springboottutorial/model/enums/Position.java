package edu.prokopchuk.springboottutorial.model.enums;

public enum Position {
  PILOT("Pilot"),
  NAVIGATOR("Navigator"),
  OPERATOR("Operator"),
  STEWARDESS("Stewardess");

  private String value;

  Position(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }

}
