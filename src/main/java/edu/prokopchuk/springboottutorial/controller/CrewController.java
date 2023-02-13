package edu.prokopchuk.springboottutorial.controller;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.service.CrewService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
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

  @ModelAttribute(name = "crewMember")
  public CrewMember crewMember() {
    return new CrewMember();
  }

  @GetMapping("/crew/new")
  public String showCreateForm() {
    return "crew-new-form";
  }

  @PostMapping("/crew")
  public String createCrewMember(@ModelAttribute("crewMember") CrewMember crewMember) {
    log.info("Created: {}", crewMember);
    crewService.createCrewMember(crewMember);

    return "redirect:/crew";
  }

}
