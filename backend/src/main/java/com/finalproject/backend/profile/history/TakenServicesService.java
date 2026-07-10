package com.finalproject.backend.profile.history;

import com.finalproject.backend.profile.DTO.ServiceDTO;
import com.finalproject.backend.repositories.ServiceRepository;
import com.finalproject.backend.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing the business logic with service history,
 * like retrieving services offered by user or services they registered
 */
@Service
public class TakenServicesService {
    private ServiceRepository serviceRepository;
    private UserRepository userRepository;

    public TakenServicesService(ServiceRepository serviceRepository, UserRepository userRepository) {
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
    }

    /**
     * Returns all services offered by a specific provider.
     * @param id id of the user.
     */
    public List<ServiceDTO> getOfferedServices(Integer id) {
        List<com.finalproject.backend.entities.Service> l = serviceRepository.findAllByProviderId(userRepository.findById(id).orElseThrow());
        return l.stream()
                .map(ServiceDTO::new)
                .toList();
    }

    /**
     * return all services for which user registered.
     * @param id  id of the user.
     * */
    public List<ServiceDTO> getRegisteredServices(Integer id) {
        List<com.finalproject.backend.entities.Service> l = serviceRepository.findRegisteredServices(id);
        return l.stream()
                .map(ServiceDTO::new)
                .toList();

    }
}