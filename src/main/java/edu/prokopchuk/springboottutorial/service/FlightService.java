package edu.prokopchuk.springboottutorial.service;

import edu.prokopchuk.springboottutorial.dao.FlightDao;
import edu.prokopchuk.springboottutorial.model.Flight;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightService {
  private FlightDao flightDao;

  @Autowired
  public FlightService(FlightDao flightDao) {
    this.flightDao = flightDao;
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
