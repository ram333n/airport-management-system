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

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.model.enums.Position;
import edu.prokopchuk.springboottutorial.service.CrewService;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(CrewController.class)
class CrewControllerTest {

  @MockBean
  private CrewService crewService;

  @Autowired
  private CrewController crewController;

  @Autowired
  private MockMvc mockMvc;

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

    Mockito.when(crewService.getAll()).thenReturn(crew);

    ResultActions resultActions = mockMvc.perform(get("/crew"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("crew"))
        .andExpect(view().name("crew"));

    String content = extractContent(resultActions);

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
  void showCreateFormWorksProperly() throws Exception {
    ResultActions resultActions = mockMvc.perform(get("/crew/new"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("crewMember"))
        .andExpect(view().name("crew-new-form"));

    String content = extractContent(resultActions);

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
        .andExpect(view().name("crew-new-form"));

    String content = extractContent(resultActions);

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
        .andExpect(view().name("redirect:/crew"))
        .andExpect(redirectedUrl("/crew"));
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
    CrewMember crewMemberAttr = CrewMember.builder()
        .passNumber("TEST-1")
        .name("Test name")
        .surname("Test surname")
        .position(Position.NAVIGATOR)
        .build();

    Mockito.when(crewService.getCrewMember("TEST-1"))
        .thenReturn(Optional.of(crewMemberAttr));

    ResultActions resultActions
        = mockMvc.perform(get("/crew/edit/{path-variable}", "TEST-1"))
        .andExpect(status().isOk())
        .andExpect(view().name("crew-edit-form"));

    String content = extractContent(resultActions);

    assertTrue(content.contains("TEST-1"));
    assertTrue(content.contains("Test name"));
    assertTrue(content.contains("Test surname"));
    assertTrue(content.contains(Position.NAVIGATOR.toString()));
  }

  @Test
  void editCrewMemberHasErrors() throws Exception {
    CrewMember crewMemberAttr = CrewMember.builder()
        .name("")
        .surname("")
        .build();

    ResultActions resultActions
        = mockMvc.perform(put("/crew").flashAttr("crewMember", crewMemberAttr))
        .andExpect(status().isOk())
        .andExpect(view().name("crew-edit-form"));

    String content = extractContent(resultActions);

    assertTrue(content.contains("Name can not be blank"));
    assertTrue(content.contains("Name must contain at least 1 character"));

    assertTrue(content.contains("Surname can not be blank"));
    assertTrue(content.contains("Name must contain at least 1 character"));
  }

  @Test
  void editCrewMemberWorksProperly() throws Exception {
    CrewMember crewMemberAttr = CrewMember.builder()
        .passNumber("TEST-1")
        .name("Test name")
        .surname("Test surname")
        .build();

    mockMvc.perform(put("/crew").flashAttr("crewMember", crewMemberAttr))
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/crew"))
        .andExpect(redirectedUrl("/crew"));
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

    Mockito.when(crewService.deleteCrewMember(passNumber)).thenReturn(true);

    mockMvc.perform(delete("/crew/{pass-number}", passNumber))
        .andExpect(status().isOk())
        .andExpect(view().name("crew"));
  }

  private String extractContent(ResultActions resultActions) throws UnsupportedEncodingException {
    return resultActions
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

}