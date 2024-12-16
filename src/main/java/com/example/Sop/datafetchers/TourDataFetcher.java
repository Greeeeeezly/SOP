package com.example.Sop.datafetchers;

import com.example.Sop.services.TourService;
import com.example.excursionbookingapi.dto.TourDto;
import com.example.excursionbookingapi.dto.TourRequest;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.List;

@DgsComponent
public class TourDataFetcher implements com.example.excursionbookingapi.datafetchers.TourDataFetcher {
    private final TourService tourService;

    public TourDataFetcher(TourService tourService) {
        this.tourService = tourService;
    }

    @DgsQuery
    public List<TourDto> tours() {
        return tourService.getAllTours();
    }


    @DgsMutation
    public TourDto addTour(@InputArgument SubmittedTour tour) {
        TourRequest newTour = new TourRequest(tour.name(), tour.availableSeats());
        TourDto createdTour = tourService.createTour(newTour);
        return createdTour;
    }

    @DgsMutation
    public TourDto updateTour(@InputArgument Long id, @InputArgument SubmittedTour tour) {
        TourDto existingTour = tourService.getTourById(id);
        if (existingTour != null) {
            existingTour.setName(tour.name());
            existingTour.setAvailableSeats(tour.availableSeats());
            return tourService.updateTour(id, existingTour);
        }
        return null;
    }

    @DgsMutation
    public void deleteTour(@InputArgument Long id) {
        tourService.deleteTour(id);
    }
}
