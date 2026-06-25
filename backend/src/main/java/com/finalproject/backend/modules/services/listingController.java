package com.finalproject.backend.modules.services;
import com.finalproject.backend.modules.services.dtos.listingFilterDto;
import com.finalproject.backend.modules.services.dtos.listingResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/listings")
public class listingController {

    @Autowired
    private listingService listingService;

    @GetMapping
    public List<listingResponseDto> getListings(listingFilterDto filter) {
        return listingService.findListings(filter);
    }
}