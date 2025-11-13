package hu.unideb.CalorieOptimization.repository;

import hu.unideb.CalorieOptimization.model.Person;
import hu.unideb.CalorieOptimization.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long>
{
    List<Person> findByUser(User user);
}
