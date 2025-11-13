package hu.unideb.CalorieOptimization.controller;

import hu.unideb.CalorieOptimization.security.CustomUserDetails;
import hu.unideb.CalorieOptimization.repository.FoodRepository;
import hu.unideb.CalorieOptimization.repository.PersonRepository;
import hu.unideb.CalorieOptimization.service.GeneticAlgorithmService;
import hu.unideb.CalorieOptimization.service.GeneticAlgorithmService;
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
    private GeneticAlgorithmService geneticAlgorithmService;

    private void loadOptimizationData(Model model, CustomUserDetails userDetails)
    {
        List<Person> peopleList = personRepository.findByUser(userDetails.getUser());
        List<Food> foodsList = foodRepository.findByUser(userDetails.getUser());
        model.addAttribute("peopleList", peopleList);
        model.addAttribute("foodsList", foodsList);
    }

    @GetMapping("/optimization")
    public String showOptimizationOptions(Model model, @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        loadOptimizationData(model, userDetails);
        return "optimization";
    }

    @PostMapping("/optimizing")
    public String Optimize(Model model, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "selectedPeople", required = false) List<Long> selectedPersonIds, @RequestParam(value = "selectedFoods", required = false) List<Long> selectedFoodIds, @RequestParam("elitismRate") int elitismRate, @RequestParam("numberOfMeals") int numberOfMeals)
    {
        if (selectedPersonIds == null || selectedPersonIds.isEmpty())
        {
            model.addAttribute("errorMessage", "Hiba! Nem választott ki embert!");
            loadOptimizationData(model, userDetails);
            return "optimization";
        }

        if (selectedFoodIds == null || selectedFoodIds.isEmpty())
        {
            model.addAttribute("errorMessage", "Hiba! Nem választott ki ételt!");
            loadOptimizationData(model, userDetails);
            return "optimization";
        }

        if (selectedFoodIds.size() < numberOfMeals)
        {
            model.addAttribute("errorMessage", "Hiba! Több étkezést választott ki, mint ételt!");
            loadOptimizationData(model, userDetails);
            return "optimization";
        }

        List<Person> selectedPeople = personRepository.findAllById(selectedPersonIds);
        List<Food> selectedFoods = foodRepository.findAllById(selectedFoodIds);

        Individual bestIndividual = geneticAlgorithmService.runGeneticAlgorithm(selectedPeople, selectedFoods, elitismRate, numberOfMeals);

        model.addAttribute("people", selectedPeople);
        model.addAttribute("chromosome", bestIndividual.getChromosome());
        model.addAttribute("fitness", bestIndividual.getFitness());

        return "results";
    }

    @GetMapping("/results")
    public String showResults()
    {
        return "results";
    }
}
