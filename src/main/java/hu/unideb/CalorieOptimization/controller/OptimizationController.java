package hu.unideb.CalorieOptimization.controller;

import hu.unideb.CalorieOptimization.security.CustomUserDetails;
import hu.unideb.CalorieOptimization.repository.FoodRepository;
import hu.unideb.CalorieOptimization.repository.PersonRepository;
import hu.unideb.CalorieOptimization.repository.UserRepository;
import hu.unideb.CalorieOptimization.service.algorithm.GeneticAlgorithm;
import hu.unideb.CalorieOptimization.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import hu.unideb.CalorieOptimization.service.algorithm.Individual;

import java.util.List;

@Controller
public class OptimizationController
{
    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/optimization")
    public String showOptimizationOptions(Model model, @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        List<Person> peopleList = personRepository.findByUser(userRepository.findByEmail(userDetails.getUsername()));
        model.addAttribute("peopleList", peopleList);

        List<Food> foodsList = foodRepository.findByUser(userRepository.findByEmail(userDetails.getUsername()));
        model.addAttribute("foodsList", foodsList);
        return "optimization";
    }

    @PostMapping("/optimizing")
    public String Optimize(Model model, @RequestParam("selectedPeople") List<Long> selectedPersonIds, @RequestParam("selectedFoods") List<Long> selectedFoodIds, @RequestParam("elitismRate") int elitismRate, @RequestParam("numberOfMeals") int numberOfMeals)
    {
        List<Person> selectedPeople = personRepository.findAllById(selectedPersonIds);

        List<Food> selectedFoods = foodRepository.findAllById(selectedFoodIds);

        if (selectedPersonIds == null || selectedPersonIds.isEmpty())
        {
            throw new IllegalArgumentException("No people selected!");
        }

        if (selectedFoodIds == null || selectedFoodIds.isEmpty())
        {
            throw new IllegalArgumentException("No foods selected!");
        }

        /////////////////////////////////////////////////////////////////
        // étkezések száma > selected foods error ha nem
        /////////////////////////////////////////////////////////////////

        GeneticAlgorithm ga = new GeneticAlgorithm(selectedPeople, selectedFoods, elitismRate, numberOfMeals);
        Individual bestSolution = ga.run();

        model.addAttribute("people", selectedPeople);
        model.addAttribute("chromosome", bestSolution.getChromosome());
        model.addAttribute("fitness", bestSolution.getFitness());

        return "results";
    }

    @GetMapping("/results")
    public String showResults()
    {
        return "results";
    }
}
