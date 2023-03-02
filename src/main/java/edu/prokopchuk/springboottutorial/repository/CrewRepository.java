package edu.prokopchuk.springboottutorial.repository;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import edu.prokopchuk.springboottutorial.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewRepository extends JpaRepository<CrewMember, String> {

  long deleteByPassNumber(String passNumber);

  Page<CrewMember> findByFlights(Flight flight, Pageable pageable);

}
