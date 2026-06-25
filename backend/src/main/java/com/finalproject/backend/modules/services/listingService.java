package com.finalproject.backend.modules.services;
import com.finalproject.backend.modules.services.dtos.listingFilterDto;
import com.finalproject.backend.modules.services.dtos.listingResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class listingService {

    @Autowired
    private listingRepository repository;

    public List<listingResponseDto> findListings(listingFilterDto filter) {
        if (filter == null) {
            return getAllListings();
        }

        List<listing> listings = repository.findByFilters(
                filter.getText(),
                filter.getCategory(),
                filter.getMin(),
                filter.getMax()
        );
        return mapToDtoList(listings);
    }

    public List<listingResponseDto> getAllListings() {
        return findListings(null);
    }

    private List<listingResponseDto> mapToDtoList(List<listing> listings) {
        List<listingResponseDto> list = new ArrayList<>();
        for (listing listing : listings) {
            list.add(convertToDto(listing));
        }
        return list;
    }

    private listingResponseDto convertToDto(listing list) {
        return new listingResponseDto(
                list.getTitle(),
                list.getBio(),
                list.getPrice(),
                list.getPictureUrl()
        );
    }
}