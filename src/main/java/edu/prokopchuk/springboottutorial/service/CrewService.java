package edu.prokopchuk.springboottutorial.service;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.repository.CrewRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
// TODO: learn about @Transactional annotation
@Transactional
public class CrewService {
  private final CrewRepository crewRepository;

  @Autowired
  public CrewService(CrewRepository crewRepository) {
    this.crewRepository = crewRepository;
  }

  public CrewMember createCrewMember(CrewMember crewMember) {
    return crewRepository.save(crewMember);
  }

  public Optional<CrewMember> getCrewMember(String passNumber) {
    return crewRepository.findById(passNumber);
  }

  public CrewMember updateCrewMember(CrewMember crewMember) {
    return crewRepository.save(crewMember);
  }

  public boolean deleteCrewMember(String passNumber) {
    return crewRepository.deleteByPassNumber(passNumber) > 0L;
  }
}
