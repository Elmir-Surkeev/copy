package com.travelagency.controller;

import com.travelagency.model.Booking;
import com.travelagency.model.Tour;
import com.travelagency.model.User;
import com.travelagency.service.BookingService;
import com.travelagency.service.TourService;
import com.travelagency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private TourService tourService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String adminDashboard(Model model) {
        model.addAttribute("tours", tourService.getAllTours());
        model.addAttribute("bookings", bookingService.getPendingBookings());
        model.addAttribute("users", userService.getAllUsers());
        return "admin/admin";
    }

    @GetMapping("/tours")
    public String manageTours(Model model) {
        List<Tour> tours = tourService.getAllTours();
        model.addAttribute("tours", tours);
        return "admin/tours";
    }

    @GetMapping("/tours/add")
    public String addTourForm(Model model) {
        model.addAttribute("tour", new Tour());
        return "admin/add-tour";
    }

    @PostMapping("/tours/add")
    public String addTour(@ModelAttribute Tour tour, Model model) {
        // Проверяем URL изображения
        if (tour.getImageUrl() != null && !tour.getImageUrl().isEmpty()) {
            try {
                // Пробуем создать URL объект для проверки корректности
                new URL(tour.getImageUrl());

                // Убеждаемся, что URL использует HTTPS
                if (!tour.getImageUrl().startsWith("https://")) {
                    tour.setImageUrl("https://" + tour.getImageUrl().substring(tour.getImageUrl().indexOf("://") + 3));
                }
            } catch (MalformedURLException e) {
                // Если URL некорректный, устанавливаем URL изображения по умолчанию
                tour.setImageUrl("/images/default-tour.jpg");
            }
        } else {
            // Если URL пустой, устанавливаем изображение по умолчанию
            tour.setImageUrl("/images/default-tour.jpg");
        }

        tourService.createTour(tour);
        return "redirect:/admin/tours";
    }


    @GetMapping("/tours/edit/{id}")
    public String editTourForm(@PathVariable Long id, Model model) {
        Tour tour = tourService.getTourById(id)
                .orElseThrow(() -> new RuntimeException("Тур не найден"));
        model.addAttribute("tour", tour);
        return "admin/edit-tour";
    }

    @PostMapping("/tours/edit")
    public String editTour(@ModelAttribute Tour tour, Model model) {
        // Та же логика проверки URL изображения
        if (tour.getImageUrl() != null && !tour.getImageUrl().isEmpty()) {
            try {
                new URL(tour.getImageUrl());
                if (!tour.getImageUrl().startsWith("https://")) {
                    tour.setImageUrl("https://" + tour.getImageUrl().substring(tour.getImageUrl().indexOf("://") + 3));
                }
            } catch (MalformedURLException e) {
                // Сохраняем текущий URL, если новый некорректный
                Tour existingTour = tourService.getTourById(tour.getId()).orElse(null);
                if (existingTour != null) {
                    tour.setImageUrl(existingTour.getImageUrl());
                } else {
                    tour.setImageUrl("/images/default-tour.jpg");
                }
            }
        }

        tourService.updateTour(tour);
        return "redirect:/admin/tours";
    }

    @GetMapping("/tours/delete/{id}")
    public String deleteTour(@PathVariable Long id) {
        tourService.deleteTour(id);
        return "redirect:/admin/tours";
    }
    @GetMapping("/bookings")
    public String manageBookings(Model model) {
        List<Booking> bookings = bookingService.getPendingBookings();
        model.addAttribute("bookings", bookings);
        return "admin/bookings";
    }

    @PostMapping("/bookings/confirm/{id}")
    public String confirmBooking(@PathVariable Long id) {
        bookingService.confirmBooking(id);
        return "redirect:/admin/bookings";
    }

    @PostMapping("/bookings/cancel/{id}")
    public String cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return "redirect:/admin/bookings";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/users/block/{id}")
    public String blockUser(@PathVariable Long id,
                            @RequestParam LocalDateTime blockedUntil) {
        userService.blockUser(id, blockedUntil);
        return "redirect:/admin/users";
    }
}