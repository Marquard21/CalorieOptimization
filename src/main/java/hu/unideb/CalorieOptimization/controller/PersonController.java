package hu.unideb.CalorieOptimization.controller;

import hu.unideb.CalorieOptimization.security.CustomUserDetails;
import hu.unideb.CalorieOptimization.model.Person;
import hu.unideb.CalorieOptimization.repository.PersonRepository;
import hu.unideb.CalorieOptimization.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PersonController
{
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/manage_people")
    public String showAllPeople(Model model, @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        List<Person> peopleList = personRepository.findByUser(userRepository.findByEmail(userDetails.getUsername()));
        model.addAttribute("peopleList", peopleList);
        return "manage_people";
    }

    @GetMapping("/manage_people/new")
    public String showCreatePersonForm(Model model)
    {
        Person person = new Person();

        person.setActivityLevel(1.2);
        person.setIntakePercentageOfDailyCalorieNeeds(100);
        person.setIntakePercentageOfProtein(30);
        person.setIntakePercentageOfCarboHydrate(40);
        person.setIntakePercentageOfFat(30);

        model.addAttribute("person", person);
        model.addAttribute("pageTitle", "Ember hozzáadása");
        return "person_form";
    }

    @GetMapping("/manage_people/edit/{id}")
    public String showEditPersonForm(@PathVariable Long id, Model model)
    {
        Person person = personRepository.findById(id).orElseThrow(() -> new RuntimeException("Person not found"));
        model.addAttribute("person", person);
        model.addAttribute("pageTitle", "Ember szerkesztése");
        return "person_form";
    }

    @PostMapping("/manage_people/save")
    public String savePerson(Person person, RedirectAttributes rd, @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        person.setUser(userRepository.findByEmail(userDetails.getUsername()));
        personRepository.save(person);
        rd.addFlashAttribute("message", "Mentés sikeres");
        return "redirect:/manage_people";
    }

    @GetMapping("/manage_people/delete/{id}")
    public String deletePerson(@PathVariable Long id, RedirectAttributes rd)
    {
        personRepository.deleteById(id);
        rd.addFlashAttribute("message", "Törlés sikeres");
        return "redirect:/manage_people";
    }
}
