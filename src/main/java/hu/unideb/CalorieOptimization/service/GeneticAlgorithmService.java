package hu.unideb.CalorieOptimization.service;

import hu.unideb.CalorieOptimization.model.Food;
import hu.unideb.CalorieOptimization.model.Person;
import hu.unideb.CalorieOptimization.service.algorithm.Individual;

import java.util.List;

public interface GeneticAlgorithmService
{
    Individual runGeneticAlgorithm(List<Person> people, List<Food> foods, int elitismRate, int numberOfMeals);
}
