package edu.prokopchuk.springboottutorial.controller;

import edu.prokopchuk.springboottutorial.service.CrewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CrewController {

  private final CrewService crewService;

  @Autowired
  public CrewController(CrewService crewService) {
    this.crewService = crewService;
  }

}
