package edu.prokopchuk.springboottutorial.service.validator.crew;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.repository.CrewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@ExtendWith(MockitoExtension.class)
class CrewMemberValidatorTest {

  @Mock
  private CrewRepository crewRepository;

  @InjectMocks
  private CrewMemberValidator validator;

  @Test
  void validatePassNumberUniquenessWhenProvidedExistentPassNumber() {
    String passNumber = "TEST-1";
    CrewMember crewMember = CrewMember.builder()
        .passNumber(passNumber)
        .build();
    Errors errors = new BeanPropertyBindingResult(crewMember, "crewMember");

    Mockito.when(crewRepository.existsById(passNumber)).thenReturn(true);

    validator.validatePassNumberUniqueness(crewMember, errors);

    assertTrue(errors.hasErrors());
    assertTrue(errors.hasFieldErrors("passNumber"));
  }

  @Test
  void validatePassNumberUniquenessWhenProvidedNonExistentPassNumber() {
    String passNumber = "TEST-1";
    CrewMember crewMember = CrewMember.builder()
        .passNumber(passNumber)
        .build();
    Errors errors = new BeanPropertyBindingResult(crewMember, "crewMember");

    Mockito.when(crewRepository.existsById(passNumber)).thenReturn(false);

    validator.validatePassNumberUniqueness(crewMember, errors);

    assertFalse(errors.hasErrors());
  }

}