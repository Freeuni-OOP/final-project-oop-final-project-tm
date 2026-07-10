package com.finalproject.backend.profile.history;

import com.finalproject.backend.profile.DTO.ServiceDTO;
import com.finalproject.backend.repositories.ServiceRepository;
import com.finalproject.backend.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TakenServicesService {
    private ServiceRepository serviceRepository;
    private UserRepository userRepository;

    public TakenServicesService(ServiceRepository serviceRepository, UserRepository userRepository) {
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
    }

    public List<ServiceDTO> getOfferedServices(Integer id) {
        List<com.finalproject.backend.entities.Service> l = serviceRepository.findAllByProviderId(userRepository.findById(id).orElseThrow());
        return l.stream()
                .map(ServiceDTO::new)
                .toList();
    }

    public List<ServiceDTO> getRegisteredServices(Integer id) {
        List<com.finalproject.backend.entities.Service> l = serviceRepository.findRegisteredServices(id);
        return l.stream()
                .map(ServiceDTO::new)
                .toList();

    }
}
