package hu.unideb.CalorieOptimization.repository;

import hu.unideb.CalorieOptimization.model.Food;
import hu.unideb.CalorieOptimization.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long>
{
    List<Food> findByUser(User user);
}
