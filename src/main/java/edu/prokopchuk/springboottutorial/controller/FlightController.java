package edu.prokopchuk.springboottutorial.controller;

import edu.prokopchuk.springboottutorial.config.FrontendProperties;
import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.service.FlightService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FlightController {

  private final FlightService flightService;
  private final FrontendProperties frontendProperties;

  @Autowired
  public FlightController(FlightService flightService,
                          FrontendProperties frontendProperties) {
    this.flightService = flightService;
    this.frontendProperties = frontendProperties;
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
