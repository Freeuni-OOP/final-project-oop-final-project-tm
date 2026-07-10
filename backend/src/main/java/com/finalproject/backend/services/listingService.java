package com.finalproject.backend.services;

import com.finalproject.backend.entities.Service; // <-- დავამატეთ Service
import com.finalproject.backend.modules.services.dtos.listingFilterDto;
import com.finalproject.backend.modules.services.dtos.listingResponseDto;
import com.finalproject.backend.repositories.listingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

//Service class for handling business logic related to listings.
@org.springframework.stereotype.Service
public class listingService {

    @Autowired
    private listingRepository repository;

    //returns a filtered and sorted list of services.
    public List<listingResponseDto> findListings(listingFilterDto filter, String sortType, String direction) {
        // Return all listings if no filter is provided
        if (filter == null) {
            return getAllListings();
        }

        // Default sorting to "id" if not provided
        String safeSortType = (sortType == null || sortType.isBlank()) ? "id" : sortType;

        // Determine sort
        Sort sort;
        if ("DESC".equalsIgnoreCase(direction)) {
            sort = Sort.by(safeSortType).descending();
        } else {
            sort = Sort.by(safeSortType).ascending();
        }

        // Fetch entities from repository
        List<Service> listings = repository.findByFilters(
                filter.getText(),
                filter.getCategory(),
                filter.getMin(),
                filter.getMax(),
                filter.getFavoriteUserId(),
                filter.getExcludeFavUserId(),
                sort
        );
        return mapToDtoList(listings);
    }

    // return list of all services mapped to DTOs.
    public List<listingResponseDto> getAllListings() {
        return mapToDtoList(repository.findAll());
    }

    // Helper method to map a list of Service entities to a list of DTOs.
    private List<listingResponseDto> mapToDtoList(List<Service> listings) {
        List<listingResponseDto> list = new ArrayList<>();
        for (Service listing : listings) {
            list.add(convertToDto(listing));
        }
        return list;
    }

    // convert single service entity into the listingResponseDto.
    private listingResponseDto convertToDto(Service list) {
        return new listingResponseDto(
                list.getId(),
                list.getTitle(),
                list.getCategory(),
                list.getBio(),
                list.getPrice(),
                list.getImagePath()
        );
    }
}