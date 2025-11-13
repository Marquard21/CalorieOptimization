package hu.unideb.CalorieOptimization.service.algorithm;

import hu.unideb.CalorieOptimization.model.Food;
import hu.unideb.CalorieOptimization.model.Person;

import java.util.*;

public class GeneticAlgorithm
{
    private final List<Person> People;

    private final List<Food> Foods;

    private final int numberOfMeals;

    private final int elitismRate;

    private static final int POPULATION_SIZE = 100;

    private static final Random random = new Random();


    public GeneticAlgorithm(List<Person> people, List<Food> foods, int elitismRate, int numberOfMeals)
    {
        this.People = people;
        this.Foods = foods;
        this.elitismRate = elitismRate;
        this.numberOfMeals = numberOfMeals;
    }

    private static int randomNumber(int start, int end)
    {
        return random.nextInt(end - start + 1) + start;
    }

    private Food selectFood(Set<Food> usedFoods)
    {
        Food food;
        do {
            food = Foods.get(randomNumber(0, Foods.size() - 1));
        } while (usedFoods.contains(food));

        return food;
    }

    private List<Individual> createInitialPopulation()
    {
        List<Individual> population = new ArrayList<>();

        for (int h = 0; h < POPULATION_SIZE; h++)
        {
            Gene[][] chromosome = new Gene[People.size()][numberOfMeals];
            List<Gene> usedAmounts = new ArrayList<>();

            for (int i = 0; i < People.size(); i++)
            {
                Set<Food> usedFoods = new HashSet<>();

                for (int j = 0; j < numberOfMeals; j++)
                {
                    Food currentFood = selectFood(usedFoods);

                    int maxAmount = currentFood.getTotalAmount();

                    for (Gene gene : usedAmounts)
                        if (gene.getFood().equals(currentFood))
                            maxAmount = Math.max(currentFood.getTotalAmount() - gene.getEaten(), 0);

                    int amount = randomNumber(0, maxAmount);

                    Gene gene = new Gene(currentFood, amount);
                    chromosome[i][j] = gene;
                    usedFoods.add(currentFood);

                    Food finalCurrentFood = currentFood;
                    Gene match = usedAmounts.stream()
                            .filter(gen -> gen.getFood().equals(finalCurrentFood))
                            .findFirst()
                            .orElse(null);

                    if (match != null)
                        match.setEaten(match.getEaten() + amount);
                    else
                        usedAmounts.add(gene);

                }
            }
            population.add(new Individual(chromosome, People));
        }

        return population;
    }

    private void mutateGene(Gene[][] chromosome, int row, int column)
    {
        chromosome[row][column] = null;
        Set<Food> usedFoods = new HashSet<>();
        Gene gene;

        for (int j = 0; j < numberOfMeals; j++)
            if (chromosome[row][j] != null)
                usedFoods.add(chromosome[row][j].getFood());

        Food currentFood = selectFood(usedFoods);

        int eaten = 0;
        for (Gene[] genes : chromosome)
            for (Gene localGene : genes)
                if (localGene != null && localGene.getFood().equals(currentFood))
                    eaten += localGene.getEaten();

        int maxAmount = Math.max(currentFood.getTotalAmount() - eaten, 0);
        int amount = randomNumber(0, maxAmount);

        gene = new Gene(currentFood, amount);
        chromosome[row][column] = gene;
    }

    private void repairOffspring(Gene[][] chromosome)
    {
        Set<Food> usedFoods = new HashSet<>();

        for (int i = 0; i < People.size(); i++)
        {
            Set<Food> used = new HashSet<>();
            for (int j = 0; j < numberOfMeals; j++)
            {
                usedFoods.add(chromosome[i][j].getFood());
                if (!used.add(chromosome[i][j].getFood()))
                {
                    Food currentFood = selectFood(used);
                    int amount = randomNumber(0, currentFood.getTotalAmount());
                    Gene gene = new Gene(currentFood, amount);
                    chromosome[i][j] = gene;
                }
            }
        }

        ArrayList<Food> setFoods = new ArrayList<>(usedFoods);
        ArrayList<Double> totals = new ArrayList<>();

        int eaten;
        for (Food setFood : setFoods)
        {
            eaten = 0;
            for (Gene[] genes : chromosome)
                for (Gene gene: genes)
                    if (gene != null && gene.getFood().equals(setFood))
                        eaten += gene.getEaten();

            totals.add((double) eaten);
        }

        for (int i = 0; i < totals.size(); i++)
        {
            if (setFoods.get(i).getTotalAmount() < totals.get(i))
                totals.set(i, totals.get(i) / setFoods.get(i).getTotalAmount());
            else
                totals.set(i, 0.0);
        }

        for (Gene[] genes : chromosome)
            for (Gene gene: genes)
                if (gene != null && setFoods.contains(gene.getFood()) && totals.get(setFoods.indexOf(gene.getFood())) != 0)
                    gene.setEaten((int) (gene.getEaten() / totals.get(setFoods.indexOf(gene.getFood()))));
    }

    private Individual crossOver(Individual parent1, Individual parent2)
    {
        Gene[][] childChromosome = new Gene[People.size()][numberOfMeals];

        for (int i = 0; i < People.size(); i++)
        {
            for (int j = 0; j < numberOfMeals; j++)
            {
                Integer p = randomNumber(1, 100);
                switch (p)
                {
                    case Integer x when x <= 45 -> childChromosome[i][j] = parent1.getChromosome()[i][j].copy();
                    case Integer x when x <= 90 -> childChromosome[i][j] = parent2.getChromosome()[i][j].copy();
                    default -> mutateGene(childChromosome, i, j);
                }
            }
        }

        repairOffspring(childChromosome);

        return new Individual(childChromosome, People);
    }

    public Individual run()
    {
        int sameBestFitnessCounter = 0;

        List<Individual> population = createInitialPopulation();
        Collections.sort(population);

        int prevBestFitness = population.get(0).getFitness();

        while (sameBestFitnessCounter < 100)
        {
            if (population.get(0).getFitness() == 0)
                break;

            List<Individual> newGeneration = new ArrayList<>();

            int s = elitismRate;
            for (int i = 0; i < POPULATION_SIZE / 100 * s; i++)
                newGeneration.add(population.get(i));


            s = POPULATION_SIZE - POPULATION_SIZE / 100 * s;
            for (int i = 0; i < s; i++)
            {
                int r = randomNumber(0, (POPULATION_SIZE / 100 * 50) - 1);
                Individual parent1 = population.get(r);
                r = randomNumber(0, (POPULATION_SIZE / 100 * 50) - 1);
                Individual parent2 = population.get(r);
                Individual offspring = crossOver(parent1, parent2);
                newGeneration.add(offspring);
            }

            population = newGeneration;
            Collections.sort(population);

            if (population.get(0).getFitness() == prevBestFitness)
                sameBestFitnessCounter++;
            else
                sameBestFitnessCounter = 0;

            prevBestFitness = population.get(0).getFitness();
        }

        return population.get(0);
    }
}
