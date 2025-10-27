package hu.unideb.CalorieOptimization.service.algorithm;

import hu.unideb.CalorieOptimization.model.Food;
import hu.unideb.CalorieOptimization.model.Person;

import java.util.List;

import static java.lang.Math.abs;

public class Individual implements Comparable<Individual>
{
    private Gene[][] chromosome; // állapot (gének sorozata == kromoszóma)
    private int fitness; // állapot fitness értéke (minél alacsonyabb annál jobb)

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

    private int calculateFitness(List<Person> People) // állapotok fitness értékének kiszámítása
    {
        int fitness = 0; // 0-ról indul a fitness

        int rows = chromosome.length;
        int columns = chromosome[0].length;


        for (int i = 0; i < rows; i++) // emberenként
        {
            double calneeds = People.get(i).getDailyCalorieNeeds() * ((double) People.get(i).getIntakePercentageOfDailyCalorieNeeds() / 100.0);
            double totalcals = 0;
            double proteins = 0;
            double carbs = 0;
            double sugar = 0;
            double fats = 0;
            double wholefats = 0;
            double fiber = 0;
            double salt = 0;

            for (int j = 0; j < columns; j++) // összes hozzárendelt étel tápanyagtartalmának kiszámítása
            {
                Food food = chromosome[i][j].getFood(); // get current food
                int eaten = chromosome[i][j].getEaten(); // get how much was eaten of current food

                totalcals += food.getEnergy()/100.0 * eaten;
                proteins += food.getProtein()/100.0  * eaten * 4.0; // 1g fehérje 4 kalória
                carbs += food.getCarboHydrate()/100.0  * eaten * 4.0; // 1g  szénhidrát 4 kalória
                sugar += food.getSugar()/100.0  * eaten;
                fats += food.getFat()/100.0  * eaten * 9.0; // 1g zsír 9 kalória
                wholefats += food.getWholeFats()/100.0  * eaten * 9.0; // 1g zsír 9 kalória
                fiber += food.getFiber()/100.0  * eaten;
                salt += food.getSalt()/100.0  * eaten;
            }

            fitness += (int) abs(calneeds - totalcals); //fitness hez hozzá adódik az eltérés a várt eredménytől
            fitness += (int) abs((calneeds * ((double) People.get(i).getIntakePercentageOfCarboHydrate() / 100.0)) - carbs); // ideális szénhidrát - állapot szénhidrát tartalma
            fitness += (int) abs((calneeds * ((double) People.get(i).getIntakePercentageOfProtein() / 100.0)) - proteins); // ideális fehérje - állapot fehérje tartalma
            fitness += (int) abs((calneeds * ((double) People.get(i).getIntakePercentageOfFat() / 100.0)) - fats); // ideális zsír - állapot zsír tartalma
            //fitness += (sugar > 50) ? (int) sugar - 50 : 0; // cukor kevesebb mint 50g naponta az ideális
            //fitness += (wholefats < (fats * 0.5)) ? (int) ((int) (fats * 0.5) - wholefats) : 0; // a zsírok fele vagy több legyen telített
            //fitness += (fiber > 50) ? (int) fiber - 50 : 0; // rostból 50g és 20g közt van az ideális
            //fitness += (fiber < 20 ) ? 20 - (int) fiber : 0; // rostból 50g és 20g közt van az ideális
            //fitness += (salt > 5) ? (int) salt - 5 : 0; // só-ból kevesebb mint 5g az ideális
        }

        return fitness;
    }

    @Override
    public int compareTo(Individual o) // a populáció sorba rendezése fitness alapján történik
    {
        return Integer.compare(this.fitness, o.fitness);
    }
}

