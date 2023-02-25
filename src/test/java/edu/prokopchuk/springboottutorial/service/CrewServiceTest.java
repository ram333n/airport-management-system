package edu.prokopchuk.springboottutorial.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.model.enums.Position;
import edu.prokopchuk.springboottutorial.repository.CrewRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    Optional<CrewMember> actual1 = crewService.getCrewMember("TEST-123");
    Optional<CrewMember> actual2 = crewService.getCrewMember("PLT-123");

    assertTrue(actual1.isEmpty());
    assertTrue(actual2.isPresent());
    assertEquals("Roman", actual2.get().getName());
  }

  @Test
  void getCrewPageWorksProperly() {
    int pageNumber = 0;
    int pageSize = 2;
    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("surname"));

    Page<CrewMember> page = crewService.getCrewPage(pageable);
    List<CrewMember> members = page.getContent();

    assertEquals(2, members.size());
    assertEquals("PLT-431", members.get(0).getPassNumber());
    assertEquals("OPR-244", members.get(1).getPassNumber());
  }

  @Test
  void updateCrewMemberWorksProperly() {
    String passNumber = "PLT-123";
    CrewMember toUpdate = CrewMember.builder()
        .passNumber(passNumber)
        .name("Ruslan")
        .surname("Test")
        .position(Position.OPERATOR)
        .build();

    crewService.updateCrewMember(toUpdate);
    Optional<CrewMember> actualOptional = crewService.getCrewMember(passNumber);

    assertTrue(actualOptional.isPresent());
    assertEquals("PLT-123", actualOptional.get().getPassNumber());
    assertEquals("Ruslan", actualOptional.get().getName());
    assertEquals("Test", actualOptional.get().getSurname());
    assertEquals(Position.OPERATOR, actualOptional.get().getPosition());
  }

  @Test
  void deleteCrewMemberWorksProperly() {
    String realPassNumber = "PLT-123";
    String fakePassNumber = "TEST-123";

    boolean isRealRemoved = crewService.deleteCrewMember(realPassNumber);
    boolean isFakeRemoved = crewService.deleteCrewMember(fakePassNumber);

    assertTrue(isRealRemoved);
    assertFalse(isFakeRemoved);
  }

}