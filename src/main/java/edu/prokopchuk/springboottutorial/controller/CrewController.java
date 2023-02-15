package edu.prokopchuk.springboottutorial.controller;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.service.CrewService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

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

  @GetMapping("/crew/new")
  public String showCreateForm(Model model) {
    model.addAttribute("crewMember", new CrewMember());

    return "crew-new-form";
  }

  @PostMapping("/crew")
  public String createCrewMember(@ModelAttribute("crewMember") CrewMember crewMember) {
    crewService.createCrewMember(crewMember);

    return "redirect:/crew";
  }

  @GetMapping("/crew/edit/{pass-number}")
  public String showEditForm(@PathVariable("pass-number") String passNumber,
                             Model model) {
    Optional<CrewMember> crewMember = crewService.getCrewMember(passNumber);

    if (crewMember.isEmpty()) {
      throw new IllegalArgumentException("Crew member with specific id not found");
    }

    model.addAttribute("crewMember", crewMember.get());

    return "crew-edit-form";
  }

  @PutMapping("/crew")
  public String editCrewMember(@ModelAttribute("crewMember") CrewMember crewMember) {
    crewService.updateCrewMember(crewMember);

    return "redirect:/crew";
  }

  @DeleteMapping("/crew/{pass-number}")
  public String deleteCrewMember(@PathVariable("pass-number") String passNumber) {
    boolean isDeleted = crewService.deleteCrewMember(passNumber);

    if (!isDeleted) {
      throw new IllegalArgumentException("Crew member with specific id not found");
    }

    return "crew";
  }

}
