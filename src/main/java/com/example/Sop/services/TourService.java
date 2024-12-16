package com.example.Sop.services;

import com.example.Sop.models.Tour;
import com.example.Sop.repositories.TourRepository;
import com.example.excursionbookingapi.dto.TourDto;
import com.example.excursionbookingapi.dto.TourRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourService {

    private final TourRepository tourRepository;
    private final ModelMapper modelMapper;

    public TourService(TourRepository tourRepository, ModelMapper modelMapper) {
        this.tourRepository = tourRepository;
        this.modelMapper = modelMapper;
    }

    public List<TourDto> getAllTours() {
        List<Tour> tours = tourRepository.findAll();
        return tours.stream()
                .map(tour -> modelMapper.map(tour, TourDto.class))
                .collect(Collectors.toList());
    }

    public TourDto getTourById(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + id));
        return modelMapper.map(tour, TourDto.class);
    }

    public TourDto createTour(TourRequest tourDto) {
        Tour tour = modelMapper.map(tourDto, Tour.class);
        Tour savedTour = tourRepository.save(tour);
        return modelMapper.map(savedTour, TourDto.class);
    }

    public TourDto updateTour(Long id, TourDto updatedTourDto) {
        Tour existingTour = tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + id));
        existingTour.setName(updatedTourDto.getName());
        existingTour.setAvailableSeats(updatedTourDto.getAvailableSeats());

        Tour updatedTour = tourRepository.save(existingTour);
        return modelMapper.map(updatedTour, TourDto.class);
    }

    public void deleteTour(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + id));

        tourRepository.delete(tour);
    }
}
