package edu.prokopchuk.springboottutorial.service;

import static org.junit.jupiter.api.Assertions.*;

import edu.prokopchuk.springboottutorial.dao.CrewDao;
import edu.prokopchuk.springboottutorial.model.CrewMember;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CrewServiceTest {
  private static CrewDao crewDao;
  private static CrewService crewService;

  @BeforeAll
  static void setUp() {
    crewDao = new CrewDao();
    crewService = new CrewService(crewDao);
  }

  @Test
  void createCrewMemberWorksProperly() {
    CrewMember actual = crewService.createCrewMember(new CrewMember());
    assertNotNull(actual);
  }

  @Test
  void getCrewMemberWorksProperly() {
    Optional<CrewMember> actual = crewService.getCrewMember("test");
    assertTrue(actual.isPresent());
  }

  @Test
  void updateCrewMemberWorksProperly() {
    CrewMember actual = crewService.updateCrewMember("test", new CrewMember());
    assertNotNull(actual);
  }

  @Test
  void deleteCrewMemberWorksProperly() {
    boolean actual = crewService.deleteCrewMember("test");
    assertTrue(actual);
  }
}