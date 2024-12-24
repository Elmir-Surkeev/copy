package com.travelagency.controller;

import com.travelagency.model.Booking;
import com.travelagency.model.Tour;
import com.travelagency.model.User;
import com.travelagency.service.BookingService;
import com.travelagency.service.TourService;
import com.travelagency.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tours")
public class TourController {
    @Autowired
    private TourService tourService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;

    @GetMapping("/{id}")
    public String tourDetails(@PathVariable Long id, Model model) {
        Tour tour = tourService.getTourById(id)
                .orElseThrow(() -> new RuntimeException("Тур не найден"));
        model.addAttribute("tour", tour);
        return "tour-details";
    }

//    @PostMapping("/book")
//    public String bookTour(@RequestParam Long tourId,
//                           @RequestParam Long userId,
//                           Model model) {
//        Tour tour = tourService.getTourById(tourId)
//                .orElseThrow(() -> new RuntimeException("Тур не найден"));
//        User user = userService.findByUsername("current_username")
//                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
//
//        Booking booking = new Booking();
//        booking.setTour(tour);
//        booking.setUser(user);
//
//        bookingService.createBooking(booking);
//        return "redirect:/bookings";
//    }


    @PostMapping("/book")
    public String bookTour(@RequestParam Long tourId,
                           HttpSession session,
                           Model model) {
        // Получаем ID пользователя из сессии
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            // Если пользователь не авторизован, перенаправляем на страницу входа
            return "redirect:/login";
        }

        Tour tour = tourService.getTourById(tourId)
                .orElseThrow(() -> new RuntimeException("Тур не найден"));

        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Booking booking = new Booking();
        booking.setTour(tour);
        booking.setUser(user);

        bookingService.createBooking(booking);
        return "redirect:/bookings";
    }
}