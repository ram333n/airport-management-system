package edu.prokopchuk.springboottutorial.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.repository.CrewRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
class CrewServiceTest {
  @MockBean
  private static CrewRepository crewRepository;

  @InjectMocks
  private static CrewService crewService;

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
    CrewMember actual = crewService.updateCrewMember(new CrewMember());
    assertNotNull(actual);
  }

  @Test
  void deleteCrewMemberWorksProperly() {
    boolean actual = crewService.deleteCrewMember("test");
    assertTrue(actual);
  }
}