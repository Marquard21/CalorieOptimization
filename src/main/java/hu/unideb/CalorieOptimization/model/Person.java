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
    private boolean sex; // férfi = 1; nő = 0

    @Column(nullable = false)
    private int height; // cm

    @Column(nullable = false)
    private int weight; // kg

    @Column(nullable = false)
    private double activityLevel; // multiplier [1, 2]

    @Column(nullable = false)
    private int intakePercentageOfDailyCalorieNeeds; // Az optimalizáció során használt kalória mennyiség hány százaléka legyen napi kalóriabeviteli szükségletnek az adott személynél

    @Column(nullable = false)
    private int intakePercentageOfProtein; // Az optimalizáció során használt kalória mennyiség hány százaléka legyen fehérje az adott személynél

    @Column(nullable = false)
    private int intakePercentageOfCarboHydrate; // Az optimalizáció során használt kalória mennyiség hány százaléka legyen szénhidrát az adott személynél

    @Column(nullable = false)
    private int intakeGramsOfSugar; // Az adott személynél megengedett cukor bevitel grammban, ugyanis minél kevesebb cukrot érdemes bevinni

    @Column(nullable = false)
    private int intakePercentageOfFat; // Az optimalizáció során használt kalória mennyiség hány százaléka legyen zsír az adott személynél

    @Column(nullable = false)
    private int intakePercentageOfWholeFats; // A bevitt zsírok hány százaléka legyen telített zsírsav az adott személynél

    @Column(nullable = false)
    private int intakeGramsOfFiber; // Az adott személyhez mennyi rost legyen kiosztva az optimalizáció során

    @Column(nullable = false)
    private int intakeGramsOfSalt; // Az adott személyhez mennyi só legyen kiosztva az optimalizáció során

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

    public int getIntakeGramsOfSugar()
    {
        return intakeGramsOfSugar;
    }

    public int getIntakePercentageOfFat()
    {
        return intakePercentageOfFat;
    }

    public int getIntakePercentageOfWholeFats()
    {
        return intakePercentageOfWholeFats;
    }

    public int getIntakeGramsOfFiber()
    {
        return intakeGramsOfFiber;
    }

    public int getIntakeGramsOfSalt()
    {
        return intakeGramsOfSalt;
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

    public void setIntakeGramsOfSugar(int intakeGramsOfSugar)
    {
        this.intakeGramsOfSugar = intakeGramsOfSugar;
    }

    public void setIntakePercentageOfFat(int intakePercentageOfFat)
    {
        this.intakePercentageOfFat = intakePercentageOfFat;
    }

    public void setIntakePercentageOfWholeFats(int intakePercentageOfWholeFats)
    {
        this.intakePercentageOfWholeFats = intakePercentageOfWholeFats;
    }

    public void setIntakeGramsOfFiber(int intakeGramsOfFiber)
    {
        this.intakeGramsOfFiber = intakeGramsOfFiber;
    }

    public void setIntakeGramsOfSalt(int intakeGramsOfSalt)
    {
        this.intakeGramsOfSalt = intakeGramsOfSalt;
    }

    public double getBMR()
    {
        LocalDate now = LocalDate.now();

        if (sex)
        {
            return (9.65 * weight) + (5.73 * height) - (5.08 * Period.between(birthDate, now).getYears()) + 260;
        }

        else
        {
            return (7.38 * weight) + (6.07 * height) - (2.31 * Period.between(birthDate, now).getYears()) + 43;
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
