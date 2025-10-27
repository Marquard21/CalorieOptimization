package hu.unideb.CalorieOptimization.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name="people")
public class Person
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length  = 20)
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private boolean sex; // male = 1; female = 0

    @Column(nullable = false)
    private int height; // cm

    @Column(nullable = false)
    private int weight; // kg

    @Column(nullable = false)
    private double activityLevel; // multiplier [1, 2]

    @Column(nullable = false)
    private int intakePercentageOfDailyCalorieNeeds; // how much of the users daily calorie needs want to use in calculation (1700kcal/day, 70% of that is 1190kcal/day)

    @Column(nullable = false)
    private int intakePercentageOfProtein; // how much of the users intakePercentageOfDailyCalorieNeeds wants it to be protein (1190kcal/day, 30% of that is going to be protein)

    @Column(nullable = false)
    private int intakePercentageOfCarboHydrate; // how much of the users intakePercentageOfDailyCalorieNeeds wants it to be carbohydrate (1190kcal/day, 40% of that is going to be carbohydrate)

    @Column(nullable = false)
    private int intakePercentageOfFat; // how much of the users intakePercentageOfDailyCalorieNeeds wants it to be fat (1190kcal/day, 30% of that is going to be fat)

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

    public LocalDate getBirthDate()
    {
        return birthDate;
    }

    public boolean isSex()
    {
        return sex;
    }

    public int getHeight()
    {
        return height;
    }

    public int getWeight()
    {
        return weight;
    }

    public double getActivityLevel()
    {
        return activityLevel;
    }

    public int getIntakePercentageOfDailyCalorieNeeds()
    {
        return intakePercentageOfDailyCalorieNeeds;
    }

    public int getIntakePercentageOfProtein()
    {
        return intakePercentageOfProtein;
    }

    public int getIntakePercentageOfCarboHydrate()
    {
        return intakePercentageOfCarboHydrate;
    }

    public int getIntakePercentageOfFat()
    {
        return intakePercentageOfFat;
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

    public void setBirthDate(LocalDate birthDate)
    {
        this.birthDate = birthDate;
    }

    public void setSex(boolean sex)
    {
        this.sex = sex;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public void setWeight(int weight)
    {
        this.weight = weight;
    }

    public void setActivityLevel(double activityLevel)
    {
        this.activityLevel = activityLevel;
    }

    public void setIntakePercentageOfDailyCalorieNeeds(int intakePercentageOfDailyCalorieNeeds)
    {
        this.intakePercentageOfDailyCalorieNeeds = intakePercentageOfDailyCalorieNeeds;
    }

    public void setIntakePercentageOfProtein(int intakePercentageOfProtein)
    {
        this.intakePercentageOfProtein = intakePercentageOfProtein;
    }

    public void setIntakePercentageOfCarboHydrate(int intakePercentageOfCarboHydrate)
    {
        this.intakePercentageOfCarboHydrate = intakePercentageOfCarboHydrate;
    }

    public void setIntakePercentageOfFat(int intakePercentageOfFat)
    {
        this.intakePercentageOfFat = intakePercentageOfFat;
    }

    public double getBMR()
    {
        LocalDate now = LocalDate.now();

        if (sex)
        {
            return (10 * weight) + (6.25 * height) - (5 * Period.between(birthDate, now).getYears()) + 5;
        }

        else
        {
            return (10 * weight) + (6.25 * height) - (5 * Period.between(birthDate, now).getYears()) - 161;
        }
    }

    public double getDailyCalorieNeeds()
    {
        return getBMR() * activityLevel;
    }

    @Override
    public String toString()
    {
        return "Person{" +
                "Name='" + name + '\'' +
                '}';
    }
}
