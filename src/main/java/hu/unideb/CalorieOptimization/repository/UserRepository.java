package hu.unideb.CalorieOptimization.repository;

import hu.unideb.CalorieOptimization.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>
{
    User findByEmail(String email);
}
