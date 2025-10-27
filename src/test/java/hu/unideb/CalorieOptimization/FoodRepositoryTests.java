package hu.unideb.CalorieOptimization;

import hu.unideb.CalorieOptimization.model.*;
import hu.unideb.CalorieOptimization.repository.FoodRepository;
import hu.unideb.CalorieOptimization.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class FoodRepositoryTests
{
    @Autowired
    private FoodRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateFood()
    {
        User user = userRepository.findByEmail("jani06@unideb.hu");

        Food food = new Food();
        food.setUser(user);
        food.setName("Tartar");
        food.setTotalAmount(715);
        food.setEnergy(6.22);
        food.setProtein(0.008);
        food.setCarboHydrate(0.044);
        food.setSugar(0.04);
        food.setFat(0.67);
        food.setWholeFats(0.052);
        food.setFiber(0.0);
        food.setSalt(0.011);

        Food savedFood = repo.save(food);

        Food existFood = entityManager.find(Food.class, savedFood.getId());

        assertThat(existFood.getName()).isEqualTo(food.getName());

    }

    @Test
    public void testFindFoodByUser()
    {
        User user = userRepository.findByEmail("jani06@unideb.hu");

        List<Food> foods = repo.findByUser(user);

        assertThat(foods.get(0)).isNotNull();
    }
}
