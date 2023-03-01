package edu.prokopchuk.springboottutorial.service.validator.crew;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.repository.CrewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class CrewMemberValidator {

  private final CrewRepository crewRepository;

  @Autowired
  public CrewMemberValidator(CrewRepository crewRepository) {
    this.crewRepository = crewRepository;
  }

  public void validatePassNumberUniqueness(CrewMember crewMember, Errors errors) {
    String passNumber = crewMember.getPassNumber();

    if (crewRepository.existsById(passNumber)) {
      errors.rejectValue("passNumber",
          "passNumberIsNotUnique",
          "Crew member with this pass number already exists");
    }
  }

}
