package edu.prokopchuk.springboottutorial.controller;

import edu.prokopchuk.springboottutorial.config.FrontendProperties;
import edu.prokopchuk.springboottutorial.exception.FlightNotFoundException;
import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.service.CrewService;
import edu.prokopchuk.springboottutorial.service.FlightService;
import edu.prokopchuk.springboottutorial.service.validator.flight.FlightValidator;
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
public class FlightController {

  private final FlightService flightService;
  private final CrewService crewService;
  private final FrontendProperties frontendProperties;
  private final FlightValidator flightValidator;

  @Autowired
  public FlightController(FlightService flightService,
                          CrewService crewService,
                          FrontendProperties frontendProperties,
                          FlightValidator flightValidator) {
    this.flightService = flightService;
    this.crewService = crewService;
    this.frontendProperties = frontendProperties;
    this.flightValidator = flightValidator;
  }

  @GetMapping("/flights")
  public String getFlights(ModelMap modelMap,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("sortby") Optional<String> sortBy) {
    int currentPageNumber = page.orElse(1);
    int pageSize = frontendProperties.getFlightsPageSize();
    String sortByField = sortBy.orElse("flightNumber");

    Sort sort = Sort.by(sortByField);
    Pageable pageable = PageRequest.of(currentPageNumber - 1, pageSize, sort);

    Page<Flight> content = flightService.getFlightPage(pageable);
    ViewUtils.fillPageWithTable(modelMap, content, "flights");

    return "flights";
  }

  //TODO: fix foreign key constraint fail
  @GetMapping("/flights/{flight-number}")
  public String getFlight(@PathVariable("flight-number") String flightNumber,
                          ModelMap modelMap,
                          @RequestParam("page") Optional<Integer> page,
                          @RequestParam("sortby") Optional<String> sortBy) {
    Optional<Flight> flightOptional = flightService.getFlight(flightNumber);

    if (flightOptional.isEmpty()) {
      throw new FlightNotFoundException(
          String.format("Flight with flight number %s not found", flightNumber)
      );
    }

    Flight flight = flightOptional.get();
    int currentPageNumber = page.orElse(1);
    int pageSize = frontendProperties.getCrewOfFlightPageSize();
    String sortByField = sortBy.orElse("passNumber");

    Sort sort = Sort.by(sortByField);
    Pageable pageable = PageRequest.of(currentPageNumber - 1, pageSize, sort);

    Page<CrewMember> crew = crewService.getCrewOfFlight(flight, pageable);

    modelMap.addAttribute("flight", flight);
    ViewUtils.fillPageWithTable(modelMap, crew, "crew");

    return "flight";
  }

  @GetMapping("/flights/new")
  public String showCreateForm(ModelMap modelMap) {
    modelMap.addAttribute("flight", new Flight());

    return "flights-new-form";
  }

  @PostMapping("/flights")
  public String createFlight(@Valid @ModelAttribute("flight") Flight flight,
                             Errors errors) {
    flightValidator.validateFlightNumberUniqueness(flight, errors);
    flightValidator.validateDurationBoundaries(flight, errors);

    if (errors.hasErrors()) {
      return "flights-new-form";
    }

    flightService.createFlight(flight);

    return "redirect:/flights/" + flight.getFlightNumber();
  }

  @GetMapping("/flights/edit/{flight-number}")
  public String showEditForm(@PathVariable("flight-number") String flightNumber,
                             ModelMap modelMap) {
    Optional<Flight> flight = flightService.getFlight(flightNumber);

    if (flight.isEmpty()) {
      throw new FlightNotFoundException(
          String.format("Flight with flight number %s not found", flightNumber)
      );
    }

    modelMap.addAttribute("flight", flight.get());

    return "flights-edit-form";
  }

  @PutMapping("/flights/{flight-number}")
  public String editFlight(@Valid @ModelAttribute("flight") Flight flight,
                           Errors errors,
                           @PathVariable("flight-number") String flightNumber) {
    flightValidator.validateDurationBoundaries(flight, errors);

    if (errors.hasErrors()) {
      return "flights-edit-form";
    }

    flightService.updateFlight(flight);

    return "redirect:/flights/" + flightNumber;
  }

  @DeleteMapping("/flights/{flight-number}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteFlight(@PathVariable("flight-number") String flightNumber) {
    boolean isDeleted = flightService.deleteFlight(flightNumber);

    if (!isDeleted) {
      throw new FlightNotFoundException(
          String.format("Flight with flight number %s not found", flightNumber)
      );
    }
  }

}
