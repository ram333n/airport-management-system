package edu.prokopchuk.springboottutorial.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import edu.prokopchuk.springboottutorial.config.FrontendProperties;
import edu.prokopchuk.springboottutorial.controller.util.TestUtils;
import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.repository.FlightRepository;
import edu.prokopchuk.springboottutorial.service.FlightService;
import edu.prokopchuk.springboottutorial.service.validator.flight.FlightValidator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@WebMvcTest(FlightController.class)
class FlightControllerTest {
  private static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm";
  private static final DateTimeFormatter FORMATTER
      = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

  @MockBean
  private FlightService flightService;

  @MockBean
  private FlightRepository flightRepository; //just to satisfy validator creation requirement

  @Autowired
  private FlightValidator flightValidator;

  @Autowired
  private FlightController flightController;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private FrontendProperties frontendProperties;

  @TestConfiguration
  static class FlightControllerTestConfig {

    @Bean
    public FrontendProperties frontendProperties() {
      return new FrontendProperties();
    }

    @Bean
    public BiFunction<String, String, String> replaceOrAddParam() {
      return (paramName, value) -> ServletUriComponentsBuilder.fromCurrentRequest()
          .replaceQueryParam(paramName, value)
          .toUriString();
    }

    @Bean
    public FlightValidator flightValidator(FlightRepository flightRepository) {
      return new FlightValidator(flightRepository);
    }

  }

  @Test
  void contextLoads() {
    assertNotNull(flightValidator);
    assertNotNull(flightController);
    assertNotNull(mockMvc);
  }

  @Test
  void showFlightsWorksProperly() throws Exception {
    LocalDateTime departureTime = LocalDateTime.now().plusHours(1L);
    LocalDateTime arrivalTime = LocalDateTime.now().plusHours(6L);

    List<Flight> flights = List.of(
        Flight.builder()
            .flightNumber("TEST-1")
            .departureFrom("Kyiv")
            .destination("Zhytomyr")
            .departureTime(departureTime)
            .arrivalTime(arrivalTime)
            .build(),

        Flight.builder()
            .flightNumber("TEST-2")
            .departureFrom("Kharkiv")
            .destination("Lviv")
            .departureTime(departureTime)
            .arrivalTime(arrivalTime)
            .build()
    );

    int pageNumber = 0;
    int pageSize = frontendProperties.getCrewPageSize();
    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("flightNumber"));

    Mockito.when(flightService.getFlightPage(pageable)).thenReturn(new PageImpl<>(flights));

    ResultActions resultActions = mockMvc.perform(get("/flights"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("flights"))
        .andExpect(view().name("flights"));

    String content = TestUtils.extractContent(resultActions);

    assertTrue(content.contains("TEST-1"));
    assertTrue(content.contains("Kyiv"));
    assertTrue(content.contains("Zhytomyr"));

    assertTrue(content.contains("TEST-2"));
    assertTrue(content.contains("Kharkiv"));
    assertTrue(content.contains("Lviv"));

    assertTrue(content.contains(departureTime.format(FORMATTER)));
    assertTrue(content.contains(arrivalTime.format(FORMATTER)));
  }

  @Test
  void showFlightThrowsAnException() throws Exception {
    String flightNumber = "TEST-1";

    Mockito.when(flightService.getFlight(flightNumber)).thenReturn(Optional.empty());

    mockMvc.perform(get("/flights/{flight-number}", flightNumber))
        .andExpect(status().isNotFound())
        .andExpect(view().name("flight-not-found"));
  }

  @Test
  void showFlightWorksProperly() throws Exception {
    String flightNumber = "TEST-1";
    LocalDateTime departureTime = LocalDateTime.now().plusHours(1L);
    LocalDateTime arrivalTime = LocalDateTime.now().plusHours(6L);
    Flight flight = Flight.builder()
        .flightNumber(flightNumber)
        .departureFrom("Kyiv")
        .destination("Odesa")
        .departureTime(departureTime)
        .arrivalTime(arrivalTime)
        .build();

    Mockito.when(flightService.getFlight(flightNumber)).thenReturn(Optional.of(flight));

    ResultActions resultActions
        = mockMvc.perform(get("/flights/{flight-number}", flightNumber))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("flight"))
        .andExpect(view().name("flight"));

    String content = TestUtils.extractContent(resultActions);

    assertTrue(content.contains("TEST-1"));
    assertTrue(content.contains("Kyiv"));
    assertTrue(content.contains("Odesa"));
    assertTrue(content.contains(departureTime.format(FORMATTER)));
    assertTrue(content.contains(arrivalTime.format(FORMATTER)));
  }

  @Test
  void showCreateFormWorksProperly() throws Exception {
    ResultActions resultActions = mockMvc.perform(get("/flights/new"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("flight"))
        .andExpect(view().name("flights-new-form"));

    String content = TestUtils.extractContent(resultActions);

    assertTrue(content.contains("Create new flight"));
  }

  @Test
  void createFlightHasErrors() throws Exception {
    Flight flightAttr = Flight.builder()
        .flightNumber("")
        .departureFrom("")
        .destination("")
        .build();

    ResultActions resultActions
        = mockMvc.perform(post("/flights").flashAttr("flight", flightAttr))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("flight"))
        .andExpect(view().name("flights-new-form"));

    String content = TestUtils.extractContent(resultActions);

    assertTrue(content.contains("Flight number can not be blank"));
    assertTrue(content.contains("Flight number must contain at least 3 characters"));

    assertTrue(content.contains("Departure city can not be blank"));
    assertTrue(content.contains("Departure city must contain at least 1 character"));

    assertTrue(content.contains("Destination city can not be blank"));
    assertTrue(content.contains("Destination city must contain at least 1 character"));

    assertTrue(content.contains("Departure time can not be empty"));

    assertTrue(content.contains("Arrival time can not be empty"));
  }

  @Test
  void createFlightWorksProperly() throws Exception {
    Flight flightAttr = Flight.builder()
        .flightNumber("TEST-1")
        .departureFrom("Kyiv")
        .destination("Sevastopol")
        .departureTime(LocalDateTime.now().plusHours(1L))
        .arrivalTime(LocalDateTime.now().plusHours(5L))
        .build();

    mockMvc.perform(post("/flights").flashAttr("flight", flightAttr))
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/flights/TEST-1"))
        .andExpect(redirectedUrl("/flights/TEST-1"));
  }

  @Test
  void showEditFormThrowsAnException() throws Exception {
    String flightNumber = "TEST-1";

    Mockito.when(flightService.getFlight(flightNumber)).thenReturn(Optional.empty());

    mockMvc.perform(get("/flights/edit/{flight-number}", flightNumber))
        .andExpect(status().isNotFound())
        .andExpect(view().name("flight-not-found"));
  }

  @Test
  void showEditFormWorksProperly() throws Exception {
    String flightNumber = "TEST-1";
    LocalDateTime departureTime = LocalDateTime.now().plusHours(1L);
    LocalDateTime arrivalTime = LocalDateTime.now().plusHours(6L);
    Flight flight = Flight.builder()
        .flightNumber(flightNumber)
        .departureFrom("Zhytomyr")
        .destination("Rivne")
        .departureTime(departureTime)
        .arrivalTime(arrivalTime)
        .build();

    Mockito.when(flightService.getFlight(flightNumber)).thenReturn(Optional.of(flight));

    ResultActions resultActions
        = mockMvc.perform(get("/flights/edit/{flight-number}", flightNumber))
        .andExpect(status().isOk())
        .andExpect(view().name("flights-edit-form"));

    String content = TestUtils.extractContent(resultActions);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    assertTrue(content.contains("TEST-1"));
    assertTrue(content.contains("Zhytomyr"));
    assertTrue(content.contains("Rivne"));
    assertTrue(content.contains(departureTime.format(formatter)));
    assertTrue(content.contains(arrivalTime.format(formatter)));
  }

  @Test
  void editFlightHasErrors() throws Exception {
    String flightNumber = "TEST-1";
    Flight flightAttr = Flight.builder()
        .flightNumber(flightNumber)
        .departureFrom("")
        .destination("")
        .build();

    ResultActions resultActions
        = mockMvc.perform(put("/flights/{flight-number}", flightNumber)
            .flashAttr("flight", flightAttr))
        .andExpect(status().isOk())
        .andExpect(view().name("flights-edit-form"));

    String content = TestUtils.extractContent(resultActions);

    assertTrue(content.contains("Departure city can not be blank"));
    assertTrue(content.contains("Departure city must contain at least 1 character"));

    assertTrue(content.contains("Destination city can not be blank"));
    assertTrue(content.contains("Destination city must contain at least 1 character"));

    assertTrue(content.contains("Departure time can not be empty"));

    assertTrue(content.contains("Arrival time can not be empty"));
  }

  @Test
  void editFlightWorksProperly() throws Exception {
    String flightNumber = "TEST-1";
    Flight flight = Flight.builder()
        .flightNumber(flightNumber)
        .departureFrom("Rivne")
        .destination("Mykolaiv")
        .departureTime(LocalDateTime.now().plusHours(1L))
        .arrivalTime(LocalDateTime.now().plusHours(7L))
        .build();

    mockMvc.perform(put("/flights/{flight-number}", flightNumber)
            .flashAttr("flight", flight))
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/flights/TEST-1"))
        .andExpect(redirectedUrl("/flights/TEST-1"));
  }

  @Test
  void deleteFlightThrowsAnException() throws Exception {
    String flightNumber = "TEST-1";

    Mockito.when(flightService.deleteFlight(flightNumber)).thenReturn(false);

    mockMvc.perform(delete("/flights/{flight-number}", flightNumber))
        .andExpect(status().isNotFound())
        .andExpect(view().name("flight-not-found"));
  }

  @Test
  void deleteFlightWorksProperly() throws Exception {
    String flightNumber = "TEST-1";
    int pageNumber = 0;
    int pageSize = frontendProperties.getFlightsPageSize();
    Pageable pageable = PageRequest.of(pageNumber, pageSize);

    Mockito.when(flightService.deleteFlight(flightNumber)).thenReturn(true);
    Mockito.when(flightService.getFlightPage(pageable)).thenReturn(new PageImpl<>(List.of()));

    mockMvc.perform(delete("/flights/{flight-number}", flightNumber))
        .andExpect(status().isNoContent());
  }

}