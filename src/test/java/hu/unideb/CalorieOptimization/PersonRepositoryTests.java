package hu.unideb.CalorieOptimization;

import hu.unideb.CalorieOptimization.model.Food;
import hu.unideb.CalorieOptimization.model.Person;
import hu.unideb.CalorieOptimization.service.algorithm.GeneticAlgorithm;
import hu.unideb.CalorieOptimization.repository.PersonRepository;
import hu.unideb.CalorieOptimization.model.User;
import hu.unideb.CalorieOptimization.repository.UserRepository;
import hu.unideb.CalorieOptimization.repository.FoodRepository;
import hu.unideb.CalorieOptimization.service.algorithm.Gene;
import hu.unideb.CalorieOptimization.service.algorithm.Individual;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class PersonRepositoryTests
{
    @Autowired
    private PersonRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodsRepository;

    @Test
    public void testCreatePerson()
    {
        User user = userRepository.findByEmail("jani06@unideb.hu");

        Person person = new Person();
        person.setUser(user);
        person.setName("Judy");
        person.setBirthDate(LocalDate.of(1979, 9, 7));
        person.setSex(false);
        person.setHeight(161);
        person.setWeight(55);
        person.setActivityLevel(1.2);

        Person savedPerson = repo.save(person);

        Person existPerson = entityManager.find(Person.class, savedPerson.getId());

        assertThat(existPerson.getName()).isEqualTo(person.getName());

    }

    @Test
    public void testFindPersonByUser()
    {
        User user = userRepository.findByEmail("jani06@unideb.hu");

        List<Person> people = repo.findByUser(user);

        assertThat(people.get(0)).isNotNull();
    }
}
