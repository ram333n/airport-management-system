package edu.prokopchuk.springboottutorial.controller;

import edu.prokopchuk.springboottutorial.config.FrontendProperties;
import edu.prokopchuk.springboottutorial.exception.CrewMemberNotFoundException;
import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.service.CrewService;
import edu.prokopchuk.springboottutorial.service.FlightService;
import edu.prokopchuk.springboottutorial.service.validator.crew.CrewMemberValidator;
import edu.prokopchuk.springboottutorial.util.ViewUtils;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@Controller
public class CrewController {

  private final CrewService crewService;
  private final FlightService flightService;
  private final FrontendProperties frontendProperties;
  private final CrewMemberValidator crewMemberValidator;

  @Autowired
  public CrewController(CrewService crewService,
                        FlightService flightService,
                        FrontendProperties frontendProperties,
                        CrewMemberValidator crewMemberValidator) {
    this.crewService = crewService;
    this.flightService = flightService;
    this.frontendProperties = frontendProperties;
    this.crewMemberValidator = crewMemberValidator;
  }

  @GetMapping("/crew")
  public String showCrew(ModelMap modelMap,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("sortby") Optional<String> sortBy) {
    int currentPageNumber = page.orElse(1);
    int pageSize = frontendProperties.getCrewPageSize();
    String sortByField = sortBy.orElse("passNumber");

    Sort sort = Sort.by(sortByField);
    Pageable pageable = PageRequest.of(currentPageNumber - 1, pageSize, sort);

    Page<CrewMember> content = crewService.getCrewPage(pageable);
    ViewUtils.fillPageWithTable(modelMap, content, "crew");

    return "crew";
  }

  @GetMapping("/crew/{pass-number}")
  public String showCrewMember(@PathVariable("pass-number") String passNumber,
                               ModelMap modelMap,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("sortby") Optional<String> sortBy) {
    Optional<CrewMember> crewMemberOptional = crewService.getCrewMember(passNumber);

    if (crewMemberOptional.isEmpty()) {
      throw new CrewMemberNotFoundException(
          String.format("Crew member with pass number %s not found", passNumber)
      );
    }

    CrewMember crewMember = crewMemberOptional.get();
    int currentPageNumber = page.orElse(1);
    int pageSize = frontendProperties.getFlightsOfCrewMemberPageSize();
    String sortByField = sortBy.orElse("flightNumber");

    Sort sort = Sort.by(sortByField);
    Pageable pageable = PageRequest.of(currentPageNumber - 1, pageSize, sort);

    Page<Flight> flights = flightService.getFlightsOfCrewMember(crewMember, pageable);

    modelMap.addAttribute("crewMember", crewMemberOptional.get());
    ViewUtils.fillPageWithTable(modelMap, flights, "flights");

    return "crew-member";
  }

  @GetMapping("/crew/new")
  public String showCreateForm(ModelMap modelMap) {
    modelMap.addAttribute("crewMember", new CrewMember());

    return "crew-new-form";
  }

  @PostMapping("/crew")
  public String createCrewMember(@Valid @ModelAttribute("crewMember") CrewMember crewMember,
                                 Errors errors) {
    crewMemberValidator.validatePassNumberUniqueness(crewMember, errors);

    if (errors.hasErrors()) {
      return "crew-new-form";
    }

    crewService.createCrewMember(crewMember);

    return "redirect:/crew/" + crewMember.getPassNumber();
  }

  @GetMapping("/crew/edit/{pass-number}")
  public String showEditForm(@PathVariable("pass-number") String passNumber,
                             ModelMap modelMap) {
    Optional<CrewMember> crewMember = crewService.getCrewMember(passNumber);

    if (crewMember.isEmpty()) {
      throw new CrewMemberNotFoundException(
          String.format("Crew member with pass number %s not found", passNumber)
      );
    }

    modelMap.addAttribute("crewMember", crewMember.get());

    return "crew-edit-form";
  }

  @PutMapping("/crew/{pass-number}")
  public String editCrewMember(@Valid @ModelAttribute("crewMember") CrewMember crewMember,
                               Errors errors,
                               @PathVariable("pass-number") String passNumber) {
    if (errors.hasErrors()) {
      return "crew-edit-form";
    }

    crewService.updateCrewMember(crewMember);

    return "redirect:/crew/" + passNumber;
  }

  @DeleteMapping("/crew/{pass-number}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCrewMember(@PathVariable("pass-number") String passNumber) {
    boolean isDeleted = crewService.deleteCrewMember(passNumber);

    if (!isDeleted) {
      throw new CrewMemberNotFoundException(
          String.format("Crew member with pass number %s not found", passNumber)
      );
    }
  }

}
