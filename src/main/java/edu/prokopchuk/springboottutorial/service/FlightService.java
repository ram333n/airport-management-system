package edu.prokopchuk.springboottutorial.service;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.repository.FlightRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlightService {

  private final FlightRepository flightRepository;

  @Autowired
  public FlightService(FlightRepository flightRepository) {
    this.flightRepository = flightRepository;
  }

  @Transactional
  public Flight createFlight(Flight flight) {
    return flightRepository.save(flight);
  }

  public Optional<Flight> getFlight(String flightNumber) {
    return flightRepository.findById(flightNumber);
  }

  public Page<Flight> getFlightPage(Pageable pageable) {
    return flightRepository.findAll(pageable);
  }

  public Page<Flight> getFlightsOfCrewMember(CrewMember crewMember, Pageable pageable) {
    return flightRepository.findByCrew(crewMember, pageable);
  }

  @Transactional
  public Flight updateFlight(Flight flight) {
    return flightRepository.save(flight);
  }

  @Transactional
  public boolean deleteFlight(String flightNumber) {
    return flightRepository.deleteByFlightNumber(flightNumber) > 0;
  }

}
