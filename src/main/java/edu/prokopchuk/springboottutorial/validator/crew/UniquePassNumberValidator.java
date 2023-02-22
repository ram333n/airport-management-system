package edu.prokopchuk.springboottutorial.validator.crew;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.repository.CrewRepository;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UniquePassNumberValidator implements Validator {

  private final CrewRepository crewRepository;

  @Autowired
  public UniquePassNumberValidator(CrewRepository crewRepository) {
    this.crewRepository = crewRepository;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Objects.equals(CrewMember.class, clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    CrewMember crewMember = (CrewMember) target;
    String passNumber = crewMember.getPassNumber();

    if (crewRepository.existsById(passNumber)) {
      errors.rejectValue("passNumber",
          "passNumber",
          "Crew member with this pass number already exists");
    }
  }

}
