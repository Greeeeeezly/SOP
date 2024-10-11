package com.example.Sop.datafetchers;

import com.example.Sop.dto.LocationDto;
import com.example.Sop.dto.TourDto;
import com.example.Sop.services.TourService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@DgsComponent
public class TourDataFetcher {
    private final TourService tourService;

    @Autowired
    public TourDataFetcher(TourService tourService) {
        this.tourService = tourService;
    }

    @DgsQuery
    public List<TourDto> tours() {
        return tourService.getAllTours();
    }

    @DgsMutation
    public TourDto addTour(@InputArgument SubmittedTour tour) {
        TourDto newTour = new TourDto(tour.title, tour.description, tour.startDate, tour.endDate);
        LocationDto locationDto = tour.location;
        newTour.setLocation(locationDto);
        newTour.setId(tourService.createTour(newTour).getId());
        return newTour;
    }

    @DgsMutation
    public TourDto updateTour(@InputArgument Long id, @InputArgument SubmittedTour tour) {
        TourDto existingTour = tourService.getTourById(id);
        if (existingTour != null) {
            existingTour.setTitle(tour.title());
            existingTour.setDescription(tour.description());
            existingTour.setLocation(tour.location());
            existingTour.setStartDate(tour.startDate());
            existingTour.setEndDate(tour.endDate());
            return tourService.updateTour(id, existingTour);
        }
        return null;
    }

    @DgsMutation
    public void deleteTour(@InputArgument Long id) {
        tourService.deleteTour(id);
    }

    record SubmittedTour(String title, String description, LocationDto location, LocalDateTime startDate, LocalDateTime endDate) {}
}
