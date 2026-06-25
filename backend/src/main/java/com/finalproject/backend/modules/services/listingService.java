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
        List<listing> rawListings = repository.findAll();

        if (filter == null) {
            return mapToDtoList(rawListings);
        }

        List<listing> filtered = new ArrayList<>();

        for (listing item : rawListings) {
            boolean matches = true;

            if (filter.getText() != null && !filter.getText().isEmpty()) {
                if (!item.getTitle().toLowerCase().contains(filter.getText().toLowerCase())) {
                    matches = false;
                }
            }
            if (filter.getMin() != null) {
                if (item.getPrice() < filter.getMin()) {
                    matches = false;
                }
            }
            if (filter.getMax() != null) {
                if (item.getPrice() > filter.getMax()) {
                    matches = false;
                }
            }
            if (filter.getCategory() != null && !filter.getCategory().isEmpty()) {
                if (!item.getCategory().equalsIgnoreCase(filter.getCategory())) {
                    matches = false;
                }
            }

            if (matches) {
                filtered.add(item);
            }
        }
        return mapToDtoList(filtered);
    }

    public List<listingResponseDto> getAllListings() {
        return findListings(null);
    }

    private List<listingResponseDto> mapToDtoList(List<listing> listings) {
        List<listingResponseDto> list = new ArrayList<>();
        for (listing entity : listings) {
            list.add(convertToDto(entity));
        }
        return list;
    }

    private listingResponseDto convertToDto(listing entity) {
        return new listingResponseDto(
                entity.getTitle(),
                entity.getBio(),
                entity.getPrice(),
                entity.getPictureUrl()
        );
    }
}