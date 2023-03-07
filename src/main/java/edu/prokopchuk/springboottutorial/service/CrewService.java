package edu.prokopchuk.springboottutorial.service;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.repository.CrewRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CrewService {

  private final CrewRepository crewRepository;

  @Autowired
  public CrewService(CrewRepository crewRepository) {
    this.crewRepository = crewRepository;
  }

  @Transactional
  public CrewMember createCrewMember(CrewMember crewMember) {
    return crewRepository.save(crewMember);
  }

  public Optional<CrewMember> getCrewMember(String passNumber) {
    return crewRepository.findById(passNumber);
  }

  public Page<CrewMember> getCrewPage(Pageable pageable) {
    return crewRepository.findAll(pageable);
  }

  public Page<CrewMember> getCrewOfFlight(Flight flight, Pageable pageable) {
    return crewRepository.findByFlights(flight, pageable);
  }

  @Transactional
  public CrewMember updateCrewMember(CrewMember crewMember) {
    return crewRepository.save(crewMember);
  }

  @Transactional
  public boolean deleteCrewMember(String passNumber) {
    return crewRepository.deleteByPassNumber(passNumber) > 0L;
  }

}
