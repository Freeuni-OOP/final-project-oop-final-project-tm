package com.finalproject.backend.services;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.modules.services.dtos.listingFilterDto;
import com.finalproject.backend.modules.services.dtos.listingResponseDto;
import com.finalproject.backend.repositories.listingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class listingServiceTest {

    @Mock
    private listingRepository repository;

    @InjectMocks
    private listingService listingService;

    @Test
    void findListings_NullFilter_ReturnsAll() {
        when(repository.findAll()).thenReturn(List.of(new Service()));

        List<listingResponseDto> result = listingService.findListings(null, "id", "ASC");

        assertFalse(result.isEmpty());
        verify(repository).findAll();
    }

    @Test
    void findListings_WithFilters_ReturnsFilteredList() {
        listingFilterDto filter = new listingFilterDto();
        when(repository.findByFilters(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(new Service()));

        List<listingResponseDto> result = listingService.findListings(filter, "price", "DESC");

        assertFalse(result.isEmpty());
        verify(repository).findByFilters(any(), any(), any(), any(), any(), any(), any(Sort.class));
    }

    @Test
    void findListings_EmptySort_DefaultsToId() {
        listingFilterDto filter = new listingFilterDto();
        when(repository.findByFilters(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(new Service()));

        listingService.findListings(filter, "", "ASC");

        verify(repository).findByFilters(any(), any(), any(), any(), any(), any(), eq(Sort.by("id").ascending()));
    }
}