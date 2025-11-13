package hu.unideb.CalorieOptimization.service.impl;

import hu.unideb.CalorieOptimization.model.Food;
import hu.unideb.CalorieOptimization.model.Person;
import hu.unideb.CalorieOptimization.service.GeneticAlgorithmService;
import hu.unideb.CalorieOptimization.service.algorithm.GeneticAlgorithm;
import hu.unideb.CalorieOptimization.service.algorithm.Individual;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneticAlgorithmServiceImpl implements GeneticAlgorithmService
{
    @Override
    public Individual runGeneticAlgorithm(List<Person> people, List<Food> foods, int elitismRate, int numberOfMeals)
    {
        GeneticAlgorithm ga = new GeneticAlgorithm(people, foods, elitismRate, numberOfMeals);
        return ga.run();
    }
}

