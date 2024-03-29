package edu.prokopchuk.springboottutorial.controller;

import edu.prokopchuk.springboottutorial.config.FrontendProperties;
import edu.prokopchuk.springboottutorial.exception.FlightNotFoundException;
import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.service.FlightService;
import edu.prokopchuk.springboottutorial.service.validator.flight.FlightValidator;
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
  private final FrontendProperties frontendProperties;
  private final FlightValidator flightValidator;

  @Autowired
  public FlightController(FlightService flightService,
                          FrontendProperties frontendProperties,
                          FlightValidator flightValidator) {
    this.flightService = flightService;
    this.frontendProperties = frontendProperties;
    this.flightValidator = flightValidator;
  }

  @GetMapping("/flights")
  public String getFlights(ModelMap modelMap,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("sortby") Optional<String> sortBy) {
    int currentPageNumber = page.orElse(1);
    String sortByField = sortBy.orElse("flightNumber");

    fillPage(modelMap, currentPageNumber, sortByField);

    return "flights";
  }

  @GetMapping("/flights/{flight-number}")
  public String getFlight(@PathVariable("flight-number") String flightNumber,
                          ModelMap modelMap) {
    Optional<Flight> flight = flightService.getFlight(flightNumber);

    if (flight.isEmpty()) {
      throw new FlightNotFoundException(
          String.format("Flight with flight number %s not found", flightNumber)
      );
    }

    modelMap.addAttribute("flight", flight.get());

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

  private void fillPage(ModelMap modelMap, int currentPageNumber, String sortByField) {
    int size = frontendProperties.getFlightsPageSize();
    Sort sort = Sort.by(sortByField);
    Pageable pageable = PageRequest.of(currentPageNumber - 1, size, sort);

    Page<Flight> page = flightService.getFlightPage(pageable);
    modelMap.addAttribute("flights", page);
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
