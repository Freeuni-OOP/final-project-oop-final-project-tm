package com.finalproject.backend.modules.services;

import com.finalproject.backend.modules.services.dtos.listingFilterDto;
import com.finalproject.backend.modules.services.dtos.listingResponseDto;
import com.finalproject.backend.services.listingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//REST Controller to handle http requests for service listings
@RestController
@RequestMapping("/api/listings")
@CrossOrigin(origins = "http://localhost:5173")
public class listingController {

    private final listingService listingService;

    // constructor injection for dependency management
    public listingController(listingService listingService) {
        this.listingService = listingService;
    }

    // fetch listings based on filters, sorting, and direction.
    @GetMapping
    public List<listingResponseDto> getListings(
            listingFilterDto filter,
            @RequestParam(defaultValue = "id") String sortType,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        // ensure filter is not null to prevent null pointer exceptions
        listingFilterDto filterToUse = (filter != null) ? filter : new listingFilterDto();

        return listingService.findListings(filterToUse, sortType, direction);
    }
}