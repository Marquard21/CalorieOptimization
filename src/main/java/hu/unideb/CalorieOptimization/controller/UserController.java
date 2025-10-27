package hu.unideb.CalorieOptimization.controller;

import hu.unideb.CalorieOptimization.model.User;
import hu.unideb.CalorieOptimization.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController
{
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model)
    {
        model.addAttribute("user", new User());
        return "registration_form";
    }

    @PostMapping("/process_register")
    public String processRegistration(User user)
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        if (userRepository.findByEmail(user.getEmail()) != null)
        {
            return "redirect:/register?error";
        }

        userRepository.save(user);

        return "registration_success";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
        {
            return "login";
        }
        return "redirect:/";
    }
}
