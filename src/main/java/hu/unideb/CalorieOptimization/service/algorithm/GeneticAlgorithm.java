package hu.unideb.CalorieOptimization.service.algorithm;

import hu.unideb.CalorieOptimization.model.Food;
import hu.unideb.CalorieOptimization.model.Person;

import java.util.*;

public class GeneticAlgorithm {
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

    private static int randomNumber(int start, int end) // véletlen szám generátor
    {
        return random.nextInt(end - start + 1) + start;
    }

    private Gene[][] createChromosome() // létrehoz egy érvényes állapotot véletlenszerűen
    {
        int numberOfPeople = People.size();
        int numberOfFoods = Foods.size();

        Gene[][] chromosome = new Gene[numberOfPeople][numberOfMeals];
        List<Gene> usedAmounts = new ArrayList<>(); // adott ételekből mennyi lett elfogyasztva más emberek által

        if (numberOfFoods < numberOfMeals) // 3 étkezés esetén 3 különböző ételnek kell szerepelnie egy embernél
        {
            throw new IllegalArgumentException("Not enough foods");
        }

        for (int i = 0; i < numberOfPeople; i++)
        {
            Set<Food> usedFoods = new HashSet<>(); // az adott ember által eddig fogyasztott ételek halmaza

            for (int j = 0; j < numberOfMeals; j++)
            {
                Food currentFood;
                do {
                    currentFood = Foods.get(randomNumber(0, numberOfFoods - 1)); // véletlenszerű étel hozzárendelése
                } while (usedFoods.contains(currentFood)); // ami még nem volt

                int amount = randomNumber(0, currentFood.getTotalAmount()); // adott ételből véletlenszerű mennyiség  hozzárendelése

                for (int k = 0; k < usedAmounts.size(); k++)
                {
                    if (usedAmounts.get(k).getFood().equals(currentFood) && amount + usedAmounts.get(k).getEaten() > currentFood.getTotalAmount()) // hogyha kevesebb étel van mint amennyit hozzá kellene rendelni
                    {
                        amount = currentFood.getTotalAmount() - usedAmounts.get(k).getEaten(); // akkor az összes maradék hozzárendelése (lehet 0)
                    }
                }

                Gene gene = new Gene(currentFood, amount);
                chromosome[i][j] = gene;
                usedFoods.add(currentFood); // az adott ember által eddig fogyasztott ételek halmazának feltöltése

                Food finalCurrentFood = currentFood; // effektíven véglegessé tett currentfood
                Gene match = usedAmounts.stream()
                        .filter(gen -> gen.getFood().equals(finalCurrentFood))
                        .findFirst()
                        .orElse(null);

                if (match != null)
                {
                    match.setEaten(match.getEaten() + amount); // adott ételekből mennyi lett elfogyasztva más emberek által listájának szerkesztése
                }

                else
                {
                    usedAmounts.add(gene); // adott ételekből mennyi lett elfogyasztva más emberek által listájának feltöltése
                }
            }
        }

        return chromosome;
    }

    private void mutateGene(Gene[][] chromosome, int row, int column) // adott kromoszóma adott helyén mutációt hajt végre figyelve arra hogy érvényes állapot jöjjön létre
    {

        int numberOfFoods = Foods.size();

        int rows = chromosome.length;
        int columns = chromosome[0].length;

        chromosome[row][column] = null;

        Set<Food> used = new HashSet<>(); // az adott ember által eddig fogyasztott ételek halmaza

        Gene gene;

        for (int j = 0; j < columns; j++)
        {
            if (chromosome[row][j] != null)
                used.add(chromosome[row][j].getFood()); // az adott ember által eddig fogyasztott ételek halmazának feltöltése
        }

        Food currentFood;
        do
        {
            currentFood = Foods.get(randomNumber(0, numberOfFoods - 1)); // véletlenszerű étel hozzárendelése
        } while (used.contains(currentFood)); // ami még nem volt

        int amount = randomNumber(0, currentFood.getTotalAmount()); // adott ételből véletlenszerű mennyiség  hozzárendelése

        int eaten = 0;
        for (int k = 0; k < rows; k++)
        {
            for (int l = 0; l < columns; l++) {
                if (chromosome[k][l] != null && chromosome[k][l].getFood().equals(currentFood))
                {
                    eaten += chromosome[k][l].getEaten(); // adott ételből mennyi lett elfogyasztva más emberek által
                }
            }
        }

        if (currentFood.getTotalAmount() - eaten < amount) // hogyha kevesebb étel van mint amennyit hozzá kellene rendelni
        {
            amount = currentFood.getTotalAmount() - eaten; // akkor az összes maradék hozzárendelése (lehet 0)
        }

        gene = new Gene(currentFood, amount);

        chromosome[row][column] = gene;
    }

    private void repairOffspring(Gene[][] chromosome) // keresztezés során létrejövő érvénytelen utód állapotok javítása
    {
        int rows = chromosome.length;
        int columns = chromosome[0].length;

        Set<Food> usedFoods = new HashSet<>(); // az adott ember által eddig fogyasztott ételek halmaza

        for (int i = 0; i < rows; i++)
        {
            Set<Food> used = new HashSet<>();
            for (int j = 0; j < columns; j++)
            {
                usedFoods.add(chromosome[i][j].getFood()); // az adott ember által eddig fogyasztott ételek halmazának feltöltése
                if (!used.add(chromosome[i][j].getFood()))
                {
                    mutateGene(chromosome, i, j); // ha 2 szer van ugyanaz az étel egy embernél akkor mutáció
                }
            }
        }

        ArrayList<Food> setfoods = new ArrayList<>(usedFoods); // mindegyik étel (egymással párhuzamos listák)
        ArrayList<Double> totals = new ArrayList<>(); // és a hozzájuk tartozó össz mennyiség (egymással párhuzamos listák)

        int eaten;
        for (int i = 0; i < setfoods.size(); i++)
        {
            eaten = 0;
            for (int k = 0; k < rows; k++)
            {
                for (int l = 0; l < columns; l++) {
                    if (chromosome[k][l] != null && chromosome[k][l].getFood().equals(setfoods.get(i)))
                    {
                        eaten += chromosome[k][l].getEaten();
                    }
                }
            }
            totals.add((double) eaten);
        }

        for (int j = 0; j < totals.size(); j++)
        {
            if (setfoods.get(j).getTotalAmount() < totals.get(j)) // hogyha egy adott ételből több van kiosztva a totals-t megváltozik egy arány számra
            {
                totals.set(j, totals.get(j) / setfoods.get(j).getTotalAmount()); // pl összesen 2500 gramm ki van osztva de csak 2000 gramm étel van akkor az arányszám 2500/2000 = 1.25 (125%) lesz
            }

            else // hogyha egy adott ételből nincs több kiosztva mint ami elérhető a totals-t 0-ra változik ezzel jelezve hogy nem kell arányszám
            {
                totals.set(j, 0.0);
            }
        }

        for (int k = 0; k < rows; k++)
        {
            for (int l = 0; l < columns; l++)
            {
                if (chromosome[k][l] != null && setfoods.contains(chromosome[k][l].getFood()) && totals.get(setfoods.indexOf(chromosome[k][l].getFood())) != 0) // hogyha totals nem nulla
                {
                    chromosome[k][l].setEaten((int) (chromosome[k][l].getEaten() / totals.get(setfoods.indexOf(chromosome[k][l].getFood())))); // az egyes emberekhez hozzárendelt étel mennyiségeket az arányszámmal osztva, a kiosztott össz mennyiség nem fogja meghaladni az összes elérhető étel mennyiséget
                }
            }
        }

    }

    private Individual crossOver(Individual parent1, Individual parent2)
    {
        int rows = People.size();
        int columns = numberOfMeals;

        Gene[][] childChromosome = new Gene[rows][columns];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++) // mátrixot bejárva
            {

                float p = randomNumber(0, 100) / 100f;

                if (p < 0.45)
                    childChromosome[i][j] = parent1.getChromosome()[i][j].clone(); // adott gén első szülőtöl jön 45%

                else if (p < 0.90)
                    childChromosome[i][j] = parent2.getChromosome()[i][j].clone(); // adott gén második szülőtöl jön 45%

                else
                    mutateGene(childChromosome, i, j); // adott gén mutálódik 10%
            }
        }

        repairOffspring(childChromosome); // az utód nem feltétlen érvényes állapot, javításra szorul

        return new Individual(childChromosome, People);
    }

    public Individual run()
    {
        int sameBestFitnessCounter = 0;

        List<Individual> population = new ArrayList<>();

        for (int i = 0; i < POPULATION_SIZE; i++) // kezdeti populáció feltöltése véletlenszerűen generált egyedekkel
        {
            Gene[][] chromosome = createChromosome();
            population.add(new Individual(chromosome, People));
        }

        Collections.sort(population); // fitness szerinti rendezés, a legjobb egyed az első helyre kerül

        int prevBestFitness = population.get(0).getFitness(); // előző generáció legjobb fitness értékének eltárolása

        while (sameBestFitnessCounter < 100) // amíg nincs tökéletes megoldás vagy 100 generáción át nem javul fitness érték
        {
            if (population.get(0).getFitness() == 0) // ha a legjobb egyed célállapot akkor leáll
            {
                break;
            }

            List<Individual> newGeneration = new ArrayList<>();

            int s = elitismRate; // a populáció X%-a
            for (int i = 0; i < s; i++)
                newGeneration.add(population.get(i)); // elitizmus miatt átkerül a következő generációba


            s = 100 - s; // a populáció 100-X %-a
            for (int i = 0; i < s; i++)
            {
                int r = randomNumber(0, 50);
                Individual parent1 = population.get(r); // top 50 egyedből kiválasztva
                r = randomNumber(0, 50);
                Individual parent2 = population.get(r); // top 50 egyedből kiválasztva
                Individual offspring = crossOver(parent1, parent2); // keresztezés, egy utódot ad
                newGeneration.add(offspring);
            }

            population = newGeneration; // új generácó lesz a populáció
            Collections.sort(population);// fitness szerinti rendezés, a legjobb egyed az első helyre kerül

            if (population.get(0).getFitness() == prevBestFitness) // javult e fitness érték
            {
                sameBestFitnessCounter++; // nem javult --> számláló +1
            }

            else
            {
                sameBestFitnessCounter = 0; // javult --> számláló visszaállítás 0-ra
            }

            prevBestFitness = population.get(0).getFitness(); // előző generáció legjobb fitness értékének eltárolása
        }

        return population.get(0);
    }
}
