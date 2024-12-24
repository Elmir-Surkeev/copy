package com.travelagency.service;


import com.travelagency.model.Tour;
import com.travelagency.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TourService {
    @Autowired
    private TourRepository tourRepository;

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Optional<Tour> getTourById(Long id) {
        return tourRepository.findById(id);
    }

    @Transactional
    public Tour createTour(Tour tour) {
        return tourRepository.save(tour);
    }

    @Transactional
    public Tour updateTour(Tour tour) {
        return tourRepository.save(tour);
    }

    @Transactional
    public void deleteTour(Long id) {
        tourRepository.deleteById(id);
    }
}
