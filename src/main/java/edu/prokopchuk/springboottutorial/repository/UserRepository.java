package edu.prokopchuk.springboottutorial.repository;

import edu.prokopchuk.springboottutorial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
