package edu.prokopchuk.springboottutorial.controller;

import edu.prokopchuk.springboottutorial.config.FrontendProperties;
import edu.prokopchuk.springboottutorial.exception.CrewMemberNotFoundException;
import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.service.CrewService;
import edu.prokopchuk.springboottutorial.service.validator.crew.UniquePassNumberValidator;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
  private final FrontendProperties frontendProperties;
  private final UniquePassNumberValidator uniquePassNumberValidator;

  @Autowired
  public CrewController(CrewService crewService,
                        FrontendProperties frontendProperties,
                        UniquePassNumberValidator uniquePassNumberValidator) {
    this.crewService = crewService;
    this.frontendProperties = frontendProperties;
    this.uniquePassNumberValidator = uniquePassNumberValidator;
  }

  @GetMapping("/crew")
  public String showCrew(ModelMap modelMap,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("sortby") Optional<String> sortBy) {
    int currentPageNumber = page.orElse(1);
    String sortByField = sortBy.orElse("passNumber");

    fillPage(modelMap, currentPageNumber, sortByField);

    return "crew";
  }

  @GetMapping("/crew/{pass-number}")
  public String showCrewMember(@PathVariable("pass-number") String passNumber,
                               ModelMap modelMap) {
    Optional<CrewMember> crewMember = crewService.getCrewMember(passNumber);

    if (crewMember.isEmpty()) {
      throw new CrewMemberNotFoundException(
          String.format("Crew member with pass number %s not found", passNumber)
      );
    }

    modelMap.addAttribute("crewMember", crewMember.get());

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
    uniquePassNumberValidator.validate(crewMember, errors);

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

  private void fillPage(ModelMap modelMap, int currentPageNumber, String sortByField) {
    int size = frontendProperties.getCrewPageSize();
    Sort sort = Sort.by(sortByField);
    Pageable pageable = PageRequest.of(currentPageNumber - 1, size, sort);

    Page<CrewMember> page = crewService.getCrewPage(pageable);
    modelMap.addAttribute("crew", page);
    modelMap.addAttribute("currentPageNumber", currentPageNumber);

    int totalPages = page.getTotalPages();

    if (totalPages > 0) {
      List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
          .boxed()
          .collect(Collectors.toList());
      modelMap.addAttribute("pageNumbers", pageNumbers);
    }
  }

}
