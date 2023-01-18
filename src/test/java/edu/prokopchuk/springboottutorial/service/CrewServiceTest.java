package edu.prokopchuk.springboottutorial.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.model.enums.Position;
import edu.prokopchuk.springboottutorial.repository.CrewRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@AutoConfigureTestDatabase
@SqlGroup({
    @Sql(value = "classpath:clear.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "classpath:test-init.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
})
class CrewServiceTest {
  @Autowired
  private CrewRepository crewRepository;

  @Autowired
  private CrewService crewService;

  @Test
  void contextLoads() {
    assertNotNull(crewRepository);
    assertNotNull(crewService);
  }

  @Test
  void createCrewMemberWorksProperly() {
    String passNumber = "TEST-123";
    CrewMember toCreate = CrewMember.builder()
        .passNumber(passNumber)
        .name("Test name")
        .surname("Test surname")
        .position(Position.PILOT)
        .build();

    crewService.createCrewMember(toCreate);
    Optional<CrewMember> actual = crewService.getCrewMember(passNumber);

    assertTrue(actual.isPresent());
    assertEquals(toCreate, actual.get());
  }

  @Test
  void getCrewMemberWorksProperly() {
    Optional<CrewMember> actual = crewService.getCrewMember("TEST-123");
    assertTrue(actual.isEmpty());
  }
//
//  @Test
//  void updateCrewMemberWorksProperly() {
//    CrewMember actual = crewService.updateCrewMember(new CrewMember());
//    assertNotNull(actual);
//  }
//
//  @Test
//  void deleteCrewMemberWorksProperly() {
//    boolean actual = crewService.deleteCrewMember("test");
//    assertTrue(actual);
//  }
}