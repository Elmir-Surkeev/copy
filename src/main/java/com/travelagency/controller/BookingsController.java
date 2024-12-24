package com.travelagency.controller;

import com.travelagency.model.Booking;
import com.travelagency.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingsController {
    @Autowired
    private BookingService bookingService;

    @GetMapping
    public String userBookings(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        List<Booking> bookings = bookingService.getUserBookings(userId);
        model.addAttribute("bookings", bookings);
        return "booking";
    }
}