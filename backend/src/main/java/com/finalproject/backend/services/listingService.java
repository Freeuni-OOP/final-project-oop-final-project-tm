package com.finalproject.backend.services;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.modules.services.dtos.listingFilterDto;
import com.finalproject.backend.modules.services.dtos.listingResponseDto;
import com.finalproject.backend.repositories.listingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class listingService {

    @Autowired
    private listingRepository repository;

    public List<listingResponseDto> findListings(listingFilterDto filter, String sortType, String direction) {
        if (filter == null) {
            return getAllListings();
        }

        String safeSortType = (sortType == null || sortType.isBlank()) ? "id" : sortType;

        Sort sort;
        if ("DESC".equalsIgnoreCase(direction)) {
            sort = Sort.by(safeSortType).descending();
        } else {
            sort = Sort.by(safeSortType).ascending();
        }

        List<Service> listings = repository.findByFilters(
                filter.getText(),
                filter.getCategory(),
                filter.getMin(),
                filter.getMax(),
                filter.getProviderId(),
                filter.getFavoriteUserId(),
                filter.getExcludeFavUserId(),
                sort
        );
        return mapToDtoList(listings);
    }

    public List<listingResponseDto> getAllListings() {
        return mapToDtoList(repository.findAll());
    }

    private List<listingResponseDto> mapToDtoList(List<Service> listings) {
        List<listingResponseDto> list = new ArrayList<>();
        for (Service listing : listings) {
            list.add(convertToDto(listing));
        }
        return list;
    }

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