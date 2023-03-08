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
import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.model.enums.Position;
import edu.prokopchuk.springboottutorial.service.CrewFlightsLinkService;
import edu.prokopchuk.springboottutorial.service.CrewService;
import edu.prokopchuk.springboottutorial.service.FlightService;
import edu.prokopchuk.springboottutorial.service.validator.crew.CrewMemberValidator;
import java.time.LocalDateTime;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@WebMvcTest(CrewController.class)
class CrewControllerTest {

  @MockBean
  private CrewService crewService;

  @MockBean
  private FlightService flightService;

  @MockBean
  private CrewFlightsLinkService crewFlightsLinkService;

  @MockBean
  private CrewMemberValidator crewMemberValidator; //just to satisfy controller creation requirement

  @Autowired
  private CrewController crewController;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private FrontendProperties frontendProperties;

  @TestConfiguration
  static class CrewControllerTestConfig {

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

    }

  @Test
  void contextLoads() {
    assertNotNull(crewService);
    assertNotNull(crewController);
    assertNotNull(mockMvc);
  }

  @Test
  void showCrewWorksProperly() throws Exception {
    List<CrewMember> crew = List.of(
        CrewMember.builder()
            .passNumber("TEST-1")
            .name("Test name 1")
            .surname("Test surname 1")
            .position(Position.PILOT)
            .build(),

        CrewMember.builder()
            .passNumber("TEST-2")
            .name("Test name 2")
            .surname("Test surname 2")
            .position(Position.OPERATOR)
            .build()
    );

    int pageNumber = 0;
    int pageSize = frontendProperties.getCrewPageSize();
    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("passNumber"));

    Mockito.when(crewService.getCrewPage(pageable))
        .thenReturn(new PageImpl<>(crew, pageable, crew.size()));

    ResultActions resultActions = mockMvc.perform(get("/crew"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("crew"))
        .andExpect(view().name("crew"));

    String content = TestUtils.extractContent(resultActions);

    assertTrue(content.contains("TEST-1"));
    assertTrue(content.contains("Test name 1"));
    assertTrue(content.contains("Test surname 1"));
    assertTrue(content.contains(Position.PILOT.toString()));

    assertTrue(content.contains("TEST-2"));
    assertTrue(content.contains("Test name 2"));
    assertTrue(content.contains("Test surname 2"));
    assertTrue(content.contains(Position.OPERATOR.toString()));
  }

  @Test
  void showCrewMemberThrowsAnException() throws Exception {
    String passNumber = "TEST-1";

    Mockito.when(crewService.getCrewMember(passNumber)).thenReturn(Optional.empty());

    mockMvc.perform(get("/crew/{pass-number}", passNumber))
        .andExpect(status().isNotFound())
        .andExpect(view().name("crew-member-not-found"));
  }

  @Test
  void showCrewMemberWorksProperly() throws Exception {
    String passNumber = "TEST-1";
    CrewMember crewMember = CrewMember.builder()
        .passNumber(passNumber)
        .name("Test name")
        .surname("Test surname")
        .position(Position.NAVIGATOR)
        .build();

    List<Flight> flights = List.of(
        Flight.builder()
            .flightNumber("TESTFLT-1")
            .departureFrom("Kyiv")
            .destination("Odesa")
            .departureTime(LocalDateTime.now().plusHours(1L))
            .arrivalTime(LocalDateTime.now().plusHours(6L))
            .build()
    );

    int pageNumber = 0;
    int pageSize = frontendProperties.getFlightsOfCrewMemberPageSize();
    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("flightNumber"));

    Mockito.when(crewService.getCrewMember(passNumber)).thenReturn(Optional.of(crewMember));
    Mockito.when(flightService.getFlightsOfCrewMember(crewMember, pageable))
        .thenReturn(new PageImpl<>(flights, pageable, flights.size()));

    ResultActions resultActions = mockMvc.perform(get("/crew/{pass-number}", passNumber))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("crewMember"))
        .andExpect(view().name("crew-member"));

    String content = TestUtils.extractContent(resultActions);

    assertTrue(content.contains("TEST-1"));
    assertTrue(content.contains("Test name"));
    assertTrue(content.contains("Test surname"));
    assertTrue(content.contains(Position.NAVIGATOR.toString()));

    assertTrue(content.contains("TESTFLT-1"));
    assertTrue(content.contains("Kyiv"));
    assertTrue(content.contains("Odesa"));
  }

  @Test
  void showCreateFormWorksProperly() throws Exception {
    ResultActions resultActions = mockMvc.perform(get("/crew/new"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("crewMember"))
        .andExpect(view().name("crew-new-form"));

    String content = TestUtils.extractContent(resultActions);

    assertTrue(content.contains("Create new crew member"));
  }

  @Test
  void createCrewMemberHasErrors() throws Exception {
    CrewMember crewMemberAttr = CrewMember.builder()
        .passNumber("")
        .name("")
        .surname("")
        .build();

    ResultActions resultActions
        = mockMvc.perform(post("/crew").flashAttr("crewMember", crewMemberAttr))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("crewMember"))
        .andExpect(view().name("crew-new-form"));

    String content = TestUtils.extractContent(resultActions);

    assertTrue(content.contains("Pass number can not be blank"));
    assertTrue(content.contains("Pass number must contain at least 3 characters"));

    assertTrue(content.contains("Name can not be blank"));
    assertTrue(content.contains("Name must contain at least 1 character"));

    assertTrue(content.contains("Surname can not be blank"));
    assertTrue(content.contains("Name must contain at least 1 character"));
  }

  @Test
  void createCrewMemberWorksProperly() throws Exception {
    CrewMember crewMemberAttr = CrewMember.builder()
        .passNumber("TEST-1")
        .name("Test name")
        .surname("Test surname")
        .position(Position.NAVIGATOR)
        .build();

    mockMvc.perform(post("/crew").flashAttr("crewMember", crewMemberAttr))
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/crew/TEST-1"))
        .andExpect(redirectedUrl("/crew/TEST-1"));
  }

  @Test
  void showEditFormThrowsAnException() throws Exception {
    String passNumber = "TEST-1";

    Mockito.when(crewService.getCrewMember(passNumber)).thenReturn(Optional.empty());

    mockMvc.perform(get("/crew/edit/{pass-number}", passNumber))
        .andExpect(status().isNotFound())
        .andExpect(view().name("crew-member-not-found"));
  }

  @Test
  void showEditFormWorksProperly() throws Exception {
    CrewMember crewMember = CrewMember.builder()
        .passNumber("TEST-1")
        .name("Test name")
        .surname("Test surname")
        .position(Position.NAVIGATOR)
        .build();

    Mockito.when(crewService.getCrewMember("TEST-1"))
        .thenReturn(Optional.of(crewMember));

    ResultActions resultActions
        = mockMvc.perform(get("/crew/edit/{path-variable}", "TEST-1"))
        .andExpect(status().isOk())
        .andExpect(view().name("crew-edit-form"));

    String content = TestUtils.extractContent(resultActions);

    assertTrue(content.contains("TEST-1"));
    assertTrue(content.contains("Test name"));
    assertTrue(content.contains("Test surname"));
    assertTrue(content.contains(Position.NAVIGATOR.toString()));
  }

  @Test
  void editCrewMemberHasErrors() throws Exception {
    String passNumber = "TEST-1";
    CrewMember crewMemberAttr = CrewMember.builder()
        .passNumber(passNumber)
        .name("")
        .surname("")
        .build();

    ResultActions resultActions
        = mockMvc.perform(put("/crew/{pass-number}", passNumber)
            .flashAttr("crewMember", crewMemberAttr))
        .andExpect(status().isOk())
        .andExpect(view().name("crew-edit-form"));

    String content = TestUtils.extractContent(resultActions);

    assertTrue(content.contains("Name can not be blank"));
    assertTrue(content.contains("Name must contain at least 1 character"));

    assertTrue(content.contains("Surname can not be blank"));
    assertTrue(content.contains("Name must contain at least 1 character"));
  }

  @Test
  void editCrewMemberWorksProperly() throws Exception {
    String passNumber = "TEST-1";
    CrewMember crewMemberAttr = CrewMember.builder()
        .passNumber(passNumber)
        .name("Test name")
        .surname("Test surname")
        .build();

    mockMvc.perform(put("/crew/{pass-number}", passNumber)
            .flashAttr("crewMember", crewMemberAttr))
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/crew/TEST-1"))
        .andExpect(redirectedUrl("/crew/TEST-1"));
  }

  @Test
  void deleteCrewMemberThrowsAnException() throws Exception {
    String passNumber = "TEST-1";

    Mockito.when(crewService.deleteCrewMember(passNumber)).thenReturn(false);

    mockMvc.perform(delete("/crew/{pass-number}", passNumber))
        .andExpect(status().isNotFound())
        .andExpect(view().name("crew-member-not-found"));
  }

  @Test
  void deleteCrewMemberWorksProperly() throws Exception {
    String passNumber = "TEST-1";
    int pageNumber = 0;
    int pageSize = frontendProperties.getCrewPageSize();
    Pageable pageable = PageRequest.of(pageNumber, pageSize);

    Mockito.when(crewService.deleteCrewMember(passNumber)).thenReturn(true);
    Mockito.when(crewService.getCrewPage(pageable)).thenReturn(new PageImpl<>(List.of()));

    mockMvc.perform(delete("/crew/{pass-number}", passNumber))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteFlightOfCrewMemberThrowsAnException() throws Exception {
    String passNumber = "TESTMBR-1";
    String flightNumber = "TESTFLT-1";

    Mockito.when(crewFlightsLinkService.unlinkCrewMemberAndFlight(passNumber, flightNumber))
        .thenReturn(false);

    mockMvc.perform(delete("/crew/{pass-number}/flights/{flight-number}", passNumber, flightNumber))
        .andExpect(status().isNotFound())
        .andExpect(view().name("no-such-flight-for-crew-member"));
  }

  @Test
  void deleteFlightOfCrewMemberWorksProperly() throws Exception {
    String passNumber = "TESTMBR-1";
    String flightNumber = "TESTFLT-1";

    Mockito.when(crewFlightsLinkService.unlinkCrewMemberAndFlight(passNumber, flightNumber))
        .thenReturn(true);

    mockMvc.perform(delete("/crew/{pass-number}/flights/{flight-number}", passNumber, flightNumber))
        .andExpect(status().isNoContent());
  }

}