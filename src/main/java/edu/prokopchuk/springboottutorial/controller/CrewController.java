package edu.prokopchuk.springboottutorial.controller;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.service.CrewService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CrewController {

  private final CrewService crewService;

  @Autowired
  public CrewController(CrewService crewService) {
    this.crewService = crewService;
  }

  @GetMapping("/crew")
  public String showCrew(Model model) {
    List<CrewMember> crew = crewService.getAll();
    model.addAttribute("crew", crew);

    return "crew";
  }

}
