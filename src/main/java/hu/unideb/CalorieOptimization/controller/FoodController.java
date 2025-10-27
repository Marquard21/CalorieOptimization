package hu.unideb.CalorieOptimization.controller;

import hu.unideb.CalorieOptimization.security.CustomUserDetails;
import hu.unideb.CalorieOptimization.repository.FoodRepository;
import hu.unideb.CalorieOptimization.model.Food;
import hu.unideb.CalorieOptimization.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class FoodController
{
    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/manage_foods")
    public String showAllFoods(Model model, @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        List<Food> foodsList = foodRepository.findByUser(userRepository.findByEmail(userDetails.getUsername()));
        model.addAttribute("foodsList", foodsList);
        return "manage_foods";
    }

    @GetMapping("/manage_foods/new")
    public String showCreateFoodForm(Model model)
    {
        model.addAttribute("food", new Food());
        model.addAttribute("pageTitle", "Étel hozzáadása");
        return "food_form";
    }

    @GetMapping("/manage_foods/edit/{id}")
    public String showEditFoodForm(@PathVariable Long id, Model model)
    {
        Food food = foodRepository.findById(id).orElseThrow(() -> new RuntimeException("food not found"));
        model.addAttribute("food", food);
        model.addAttribute("pageTitle", "Étel szerkesztése");
        return "food_form";
    }

    @PostMapping("/manage_foods/save")
    public String saveFood(Food food, RedirectAttributes rd, @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        food.setUser(userRepository.findByEmail(userDetails.getUsername()));
        foodRepository.save(food);
        rd.addFlashAttribute("message", "Mentés sikeres");
        return "redirect:/manage_foods";
    }

    @GetMapping("/manage_foods/delete/{id}")
    public String deleteFood(@PathVariable Long id, RedirectAttributes rd)
    {
        foodRepository.deleteById(id);
        rd.addFlashAttribute("message", "Törlés sikeres");
        return "redirect:/manage_foods";
    }
}
