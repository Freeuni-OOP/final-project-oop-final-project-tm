package com.finalproject.backend.modules.services;

import com.finalproject.backend.modules.services.dtos.listingFilterDto;
import com.finalproject.backend.modules.services.dtos.listingResponseDto;
import com.finalproject.backend.services.listingService;
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
    public List<listingResponseDto> getListings(
            listingFilterDto filter,
            @RequestParam(defaultValue = "id") String sortType,      // 👈 თუ არ მოვა, ჩაჯდება "id"
            @RequestParam(defaultValue = "ASC") String direction     // 👈 თუ არ მოვა, ჩაჯდება "ASC"
    ) {
        listingFilterDto filterToUse = (filter != null) ? filter : new listingFilterDto();

        return listingService.findListings(filterToUse, sortType, direction);
    }
}