package com.finalproject.backend.modules.services;
import com.finalproject.backend.modules.services.dtos.listingFilterDto;
import com.finalproject.backend.modules.services.dtos.listingResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listings")
@CrossOrigin(origins = "http://localhost:5173")
public class listingController {

    private final listingService listingService;

    public listingController(listingService listingService) {
        this.listingService = listingService;
    }

    @GetMapping
    public List<listingResponseDto> getListings(listingFilterDto filter, String sortType, String direction) {
        listingFilterDto filterToUse;
        if (filter != null) {
            filterToUse = filter;
        } else {
            filterToUse = new listingFilterDto();
        }
        return listingService.findListings(filterToUse, sortType, direction);
    }
}