package hu.unideb.CalorieOptimization.model;

import jakarta.persistence.*;

@Entity
@Table(name="foods")
public class Food
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length  = 20)
    private String name;

    @Column(nullable = false)
    private int totalAmount; // grams

    @Column(nullable = false)
    private double energy; // kcal/100g

    @Column(nullable = false)
    private double protein; // g/100g

    @Column(nullable = false)
    private double carboHydrate; // g/100g

    @Column(nullable = false)
    private double sugar; // g/100g

    @Column(nullable = false)
    private double fat; // g/100g

    @Column(nullable = false)
    private double wholeFats; // g/100g

    @Column(nullable = false)
    private double fiber; // g/100g

    @Column(nullable = false)
    private double salt; // g/100g

    public Long getId()
    {
        return id;
    }

    public User getUser()
    {
        return user;
    }

    public String getName()
    {
        return name;
    }

    public int getTotalAmount()
    {
        return totalAmount;
    }

    public double getEnergy()
    {
        return energy;
    }

    public double getProtein()
    {
        return protein;
    }

    public double getCarboHydrate()
    {
        return carboHydrate;
    }

    public double getSugar()
    {
        return sugar;
    }

    public double getFat()
    {
        return fat;
    }

    public double getWholeFats()
    {
        return wholeFats;
    }

    public double getFiber()
    {
        return fiber;
    }

    public double getSalt()
    {
        return salt;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setTotalAmount(int totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public void setEnergy(double energy)
    {
        this.energy = energy;
    }

    public void setProtein(double protein)
    {
        this.protein = protein;
    }

    public void setCarboHydrate(double carboHydrate)
    {
        this.carboHydrate = carboHydrate;
    }

    public void setSugar(double sugar)
    {
        this.sugar = sugar;
    }

    public void setFat(double fat)
    {
        this.fat = fat;
    }

    public void setWholeFats(double wholeFats)
    {
        this.wholeFats = wholeFats;
    }

    public void setFiber(double fiber)
    {
        this.fiber = fiber;
    }

    public void setSalt(double salt)
    {
        this.salt = salt;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
