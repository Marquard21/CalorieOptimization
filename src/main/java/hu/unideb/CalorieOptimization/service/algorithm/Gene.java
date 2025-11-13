package hu.unideb.CalorieOptimization.service.algorithm;

import hu.unideb.CalorieOptimization.model.Food;

public class Gene
{
    Food food;

    int eaten;

    public Gene(Food food, int eaten)
    {
        this.food = food;
        this.eaten = eaten;
    }

    public Food getFood()
    {
        return food;
    }

    public int getEaten()
    {
        return eaten;
    }

    public void setEaten(int eaten)
    {
        this.eaten = eaten;
    }

    public Gene copy()
    {
        return new Gene(this.food, this.eaten);
    }

    @Override
    public String toString()
    {
        return food + ": " + eaten + "g";
    }
}
