package edu.prokopchuk.springboottutorial.service;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.repository.CrewRepository;
import edu.prokopchuk.springboottutorial.repository.FlightRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrewFlightsLinkService {

  private final CrewRepository crewRepository;
  private final FlightRepository flightRepository;

  @Autowired
  public CrewFlightsLinkService(CrewRepository crewRepository, FlightRepository flightRepository) {
    this.crewRepository = crewRepository;
    this.flightRepository = flightRepository;
  }

  @Transactional
  public boolean linkCrewMemberAndFlight(String passNumber, String flightNumber) {
    Optional<CrewMember> crewMember = crewRepository.findById(passNumber);
    Optional<Flight> flight = flightRepository.findById(flightNumber);

    if (crewMember.isEmpty() || flight.isEmpty()) {
      return false;
    }

    crewMember.get().addFlight(flight.get());

    return true;
  }

  @Transactional
  public boolean unlinkCrewMemberAndFlight(String passNumber, String flightNumber) {
    Optional<CrewMember> crewMember = crewRepository.findById(passNumber);
    Optional<Flight> flight = flightRepository.findById(flightNumber);

    if (crewMember.isEmpty() || flight.isEmpty()) {
      return false;
    }

    return crewMember.get().removeFlight(flight.get());
  }

}
