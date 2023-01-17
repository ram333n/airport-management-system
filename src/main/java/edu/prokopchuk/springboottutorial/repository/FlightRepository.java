package edu.prokopchuk.springboottutorial.repository;

import edu.prokopchuk.springboottutorial.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String> {

}
