package edu.prokopchuk.springboottutorial.exception;

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
    ModelAndView mav = new ModelAndView();
    mav.setViewName("crew-member-not-found");

    return mav;
  }

}
