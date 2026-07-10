package com.finalproject.backend.servicePages.logic;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.StarID;
import com.finalproject.backend.entities.Stars;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.ServiceRepository;
import com.finalproject.backend.repositories.StarRepository;
import com.finalproject.backend.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceManager {

    private final ServiceRepository serviceRepository;
    private final StarRepository starRepository;
    private final UserRepository userRepository;
    // Constructor injection matching your AuthController style
    public ServiceManager(ServiceRepository serviceRepository,
                          StarRepository starRepository,
                          UserRepository userRepository) {
        this.serviceRepository = serviceRepository;
        this.starRepository = starRepository;
        this.userRepository = userRepository;
    }

    public  Map<String, Object> getServiceInformation(int serviceId) {
        // 1. Fetch from the database
        Optional<Service> serviceOptional = serviceRepository.findById(serviceId);

        // 2. Handle the case where the service isn't found (returns a 404)
        if (serviceOptional.isEmpty()) {
            return null;
        }

        // 3. Get the actual Service object
        Service service = serviceOptional.get();

        // 4. Create a custom JSON response with only the fields you requested
        Map<String, Object> responseJson = new HashMap<>();

        responseJson.put("serviceId", service.getId());
        responseJson.put("serviceBio", service.getBio());
        responseJson.put("serviceImage", service.getImagePath());
        responseJson.put("serviceTitle", service.getTitle());
        responseJson.put("servicePrice", service.getPrice());
        responseJson.put("serviceCategory", service.getCategory());
        responseJson.put("serviceAddress", service.getAddress());
        responseJson.put("serviceProfileId", service.getProviderId().getId());

        return responseJson;
    }

    public Map<String, Boolean> getStarEssence(int serviceId,int userId){
        StarID starID = new StarID(userId,serviceId);
        return Map.of("stared",starRepository.existsById(starID));
    }

    public Map<String, Integer> getStarNumber(int serviceId){
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(()->new RuntimeException("Service Not Found"));
        int userId = service.getProviderId().getId();
        return Map.of("starNum", starRepository.countByStarred(service));
    }

    @Transactional
    public void addStar(int serviceId,int userId){
            Stars star = new Stars();
            StarID starID = new StarID(userId,serviceId);
            if(starRepository.existsById(starID)){
                return;
            }
            User user = userRepository.findById(userId)
                    .orElseThrow(()->new RuntimeException("User Not Found"));
            Service service = serviceRepository.findById(serviceId)
                    .orElseThrow(()->new RuntimeException("Service Not Found"));
            star.setStarID(starID);
            star.setStarer(user);
            star.setStarred(service);
            starRepository.save(star);
            System.out.println("star added");
            service.setStar(service.getStar()+1);
            serviceRepository.save(service);
            System.out.println("star num increased in service");
    }

    public void removeStar(int serviceId,int userId){
        Stars star = new Stars();
        StarID starID = new StarID(userId,serviceId);
        if(!starRepository.existsById(starID)){
            return;
        }
        User user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User Not Found"));
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(()->new RuntimeException("Service Not Found"));
        star.setStarID(starID);
        star.setStarer(user);
        star.setStarred(service);
        starRepository.delete(star);
        System.out.println("star deleted");
        service.setStar(service.getStar()-1);
        serviceRepository.save(service);
        System.out.println("star num decreased in service");
    }

    public Map<String, Object> getServiceImagePath(int serviceId) {
        // 1. Fetch from the database
        Optional<Service> serviceOptional = serviceRepository.findById(serviceId);

        // 2. Handle the case where the service isn't found (returns a 404)
        if (serviceOptional.isEmpty()) {
            return null;
        }

        // 3. Get the actual Service object
        Service service = serviceOptional.get();

        // 4. Create a custom JSON response with ONLY the image path
        Map<String, Object> responseJson = new HashMap<>();

        // CRITICAL: The key must be "imagePath" to match your React frontend!
        responseJson.put("imagePath", service.getImagePath());

        return responseJson;
    }
}
