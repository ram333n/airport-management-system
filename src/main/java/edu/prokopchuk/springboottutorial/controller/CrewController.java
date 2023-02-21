package edu.prokopchuk.springboottutorial.controller;

import edu.prokopchuk.springboottutorial.config.FrontendProperties;
import edu.prokopchuk.springboottutorial.exception.CrewMemberNotFoundException;
import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.service.CrewService;
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
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class CrewController {

  private final CrewService crewService;
  private final FrontendProperties frontendProperties;

  @Autowired
  public CrewController(CrewService crewService, FrontendProperties frontendProperties) {
    this.crewService = crewService;
    this.frontendProperties = frontendProperties;
  }

  @GetMapping("/crew")
  public String showCrew(ModelMap modelMap,
                         @RequestParam("page") Optional<Integer> pageNumber) {
    int currentPageNumber = pageNumber.orElse(1);
    fillPage(modelMap, currentPageNumber);

    return "crew";
  }

  @GetMapping("/crew/new")
  public String showCreateForm(ModelMap modelMap) {
    modelMap.addAttribute("crewMember", new CrewMember());

    return "crew-new-form";
  }

  @PostMapping("/crew")
  public String createCrewMember(@Valid @ModelAttribute("crewMember") CrewMember crewMember,
                                 Errors errors) {
    if (errors.hasErrors()) {
      return "crew-new-form";
    }

    crewService.createCrewMember(crewMember);

    return "redirect:/crew";
  }

  @GetMapping("/crew/edit/{pass-number}")
  public String showEditForm(@PathVariable("pass-number") String passNumber,
                             ModelMap modelMap) {
    Optional<CrewMember> crewMember = crewService.getCrewMember(passNumber);

    if (crewMember.isEmpty()) {
      throw new CrewMemberNotFoundException("Crew member with given id not found");
    }

    modelMap.addAttribute("crewMember", crewMember.get());

    return "crew-edit-form";
  }

  @PutMapping("/crew")
  public String editCrewMember(@Valid @ModelAttribute("crewMember") CrewMember crewMember,
                               Errors errors) {
    if (errors.hasErrors()) {
      return "crew-edit-form";
    }

    crewService.updateCrewMember(crewMember);

    return "redirect:/crew";
  }

  @DeleteMapping("/crew/{pass-number}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteCrewMember(@PathVariable("pass-number") String passNumber) {
    boolean isDeleted = crewService.deleteCrewMember(passNumber);

    if (!isDeleted) {
      throw new CrewMemberNotFoundException("Crew member with given id not found");
    }
  }

  private void fillPage(ModelMap modelMap, int currentPageNumber) {
    int size = frontendProperties.getCrewPageSize();
    Pageable pageable = PageRequest.of(currentPageNumber - 1, size);

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
