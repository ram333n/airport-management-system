package edu.prokopchuk.springboottutorial.repository;

import edu.prokopchuk.springboottutorial.model.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewRepository extends JpaRepository<CrewMember, String> {

  long deleteByPassNumber(String passNumber);

}
