package edu.prokopchuk.springboottutorial.service;

import edu.prokopchuk.springboottutorial.model.Flight;
import edu.prokopchuk.springboottutorial.repository.FlightRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightService {
  private FlightRepository flightRepository;

  @Autowired
  public FlightService(FlightRepository flightRepository) {
    this.flightRepository = flightRepository;
  }

  public Flight createFlight(Flight flight) {
    return new Flight();
  }

  public Optional<Flight> getFlight(String flightNumber) {
    return Optional.of(new Flight());
  }

  public Flight updateFlight(String flightNumber, Flight flight) {
    return new Flight();
  }

  public boolean deleteFlight(String flightNumber) {
    return true;
  }
}
