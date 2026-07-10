package com.finalproject.backend.services;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.profile.DTO.ServiceDTO;
import com.finalproject.backend.repositories.ServiceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HistoryService {

    private final ServiceRepository serviceRepository;

    public HistoryService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<ServiceDTO> getOfferedServices(Integer userId) {
        return serviceRepository.findByProviderId(userId)
                .stream()
                .map(s -> new ServiceDTO(
                        s.getId(),
                        s.getTitle(),
                        s.getCategory(),
                        s.getPrice(),
                        "OFFERED"
                ))
                .collect(Collectors.toList());
    }

    public List<ServiceDTO> getRegisteredServices(Integer userId) {
        return serviceRepository.findRegisteredServicesByUserId(userId)
                .stream()
                .map(row -> new ServiceDTO(
                        // service_id
                        (Integer) row[0],

                        // title
                        (String)  row[1],

                        // category
                        (String)  row[2],

                        // price
                        row[3] != null ? ((Number) row[3]).doubleValue() : null,

                        // status
                        (String)  row[4]
                ))
                .collect(Collectors.toList());
    }
}