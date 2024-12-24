package com.travelagency.controller;

import com.travelagency.model.User;
import com.travelagency.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        try {
            User user = userService.findByUsername(username)
                    .orElse(null);

            System.out.println("Login attempt for user: " + username); // Для отладки

            if (user != null) {
                System.out.println("User found, role: " + user.getRole()); // Для отладки
                if (userService.checkPassword(user, password)) {
                    // Сохраняем информацию о пользователе в сессии
                    session.setAttribute("userId", user.getId());
                    session.setAttribute("userRole", user.getRole().name()); // Сохраняем как строку
                    session.setAttribute("username", user.getUsername());

                    System.out.println("Login successful, redirecting..."); // Для отладки

                    // Проверяем роль и перенаправляем
                    if (user.getRole() == User.Role.ADMIN) {
                        return "redirect:/admin/tours";
                    }
                    return "redirect:/";
                }
            }

            model.addAttribute("error", "Неверное имя пользователя или пароль");
            return "login";

        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage()); // Для отладки
            model.addAttribute("error", "Произошла ошибка при входе");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        // Проверка, существует ли пользователь
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Пользователь с таким именем уже существует");
            return "register";
        }

        user.setRole(User.Role.CLIENT);
        userService.registerUser(user);
        return "redirect:/login";
    }
}