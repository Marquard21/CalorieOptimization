package hu.unideb.CalorieOptimization.service.algorithm;

import hu.unideb.CalorieOptimization.model.Food;
import hu.unideb.CalorieOptimization.model.Person;

import java.util.List;

import static java.lang.Math.abs;

public class Individual implements Comparable<Individual>
{
    private final Gene[][] chromosome;
    private final int fitness;

    public Individual(Gene[][] chromosome, List<Person> People)
    {
        this.chromosome = chromosome;
        fitness = calculateFitness(People);
    }

    public Gene[][] getChromosome()
    {
        return chromosome;
    }

    public int getFitness()
    {
        return fitness;
    }

    private int calculateFitness(List<Person> People)
    {
        int fitness = 0;

        for (int i = 0; i < chromosome.length; i++)
        {
            Person person = People.get(i);

            double calNeeds = person.getDailyCalorieNeeds() / 100  * person.getIntakePercentageOfDailyCalorieNeeds();
            double totalCals = 0; // kcal
            double proteins = 0; // kcal
            double carbs = 0; // kcal
            double sugar = 0; // g
            double fats = 0; // kcal
            double wholeFats = 0; // kcal
            double fiber = 0; // g
            double salt = 0; // g

            for (int j = 0; j < chromosome[0].length; j++)
            {
                Food food = chromosome[i][j].getFood();
                int eaten = chromosome[i][j].getEaten();

                totalCals += food.getEnergy() / 100 * eaten;
                proteins += food.getProtein() / 100  * eaten * 4.0; // 1g fehérje 4 kalória
                carbs += food.getCarboHydrate() / 100  * eaten * 4.0; // 1g  szénhidrát 4 kalória
                sugar += food.getSugar() / 100  * eaten;
                fats += food.getFat() / 100  * eaten * 9.0; // 1g zsír 9 kalória
                wholeFats += food.getWholeFats() / 100  * eaten * 9.0; // 1g zsír 9 kalória
                fiber += food.getFiber() / 100  * eaten;
                salt += food.getSalt() / 100  * eaten;
            }

            double calculatedCals = proteins + carbs + fats;

            double bigger = Math.max(totalCals, calculatedCals);
            double smaller = Math.min(totalCals, calculatedCals);
            if (calNeeds > bigger)
                fitness += (int) Math.abs(calNeeds - bigger);
            else if (calNeeds < smaller)
                fitness += (int) Math.abs(calNeeds - smaller);

            fitness += (int) abs(carbs / calNeeds * 100 - person.getIntakePercentageOfCarboHydrate());
            fitness += (int) abs(proteins / calNeeds * 100 - person.getIntakePercentageOfProtein());
            fitness += sugar > person.getIntakeGramsOfSugar() ? (int) sugar - person.getIntakeGramsOfSugar() : 0;
            fitness += (int) abs(fats / calNeeds * 100 - person.getIntakePercentageOfFat());
            fitness += (int) abs(wholeFats / fats  * 100 - person.getIntakePercentageOfWholeFats());
            fitness += (int) abs(fiber - person.getIntakeGramsOfFiber());
            fitness += (int) abs(salt - person.getIntakeGramsOfSalt());
        }

        return fitness;
    }

    @Override
    public int compareTo(Individual o)
    {
        return Integer.compare(this.fitness, o.fitness);
    }
}

