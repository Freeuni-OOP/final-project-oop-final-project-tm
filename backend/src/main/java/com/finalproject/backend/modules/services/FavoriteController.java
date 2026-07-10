package com.finalproject.backend.modules.services;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.repositories.FavoriteRepository;
import com.finalproject.backend.repositories.UserModuleRepository;
import com.finalproject.backend.repositories.listingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for managing user favorite services
 * does adding and removing services from favorites and returning specific  user lists.
 */
@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;
    private final UserModuleRepository userRepository;
    private final listingRepository listingRepository;

    public FavoriteController(FavoriteRepository favRep, UserModuleRepository userRep, listingRepository listRep) {
        this.favoriteRepository = favRep;
        this.userRepository = userRep;
        this.listingRepository = listRep;
    }

    /**
     * Toggles the favorite status for a service.
     * If the favorite exists remove it, in other case it is created.
     * @param userId id of the user.
     * @param serviceId if of the service.
     */
    @PostMapping("/{userId}/{serviceId}")
    @Transactional
    public ResponseEntity<String> toggleFavorite(@PathVariable Integer userId, @PathVariable Integer serviceId) {
        // Check if relationship exists
        if (favoriteRepository.existsByUserIdAndServiceId(userId, serviceId)) {
            favoriteRepository.deleteByUserIdAndServiceId(userId, serviceId);
            return ResponseEntity.ok("Removed");
        }
        // if it doesnt exist create a new favorite entity
        Favorite fav = new Favorite();
        fav.setUser(userRepository.findById(userId).orElseThrow());
        fav.setService(listingRepository.findById(serviceId).orElseThrow());
        favoriteRepository.save(fav);

        return ResponseEntity.ok("Added");
    }

    /**
     * Returns list ofservices favorited by user.
     * @param userId id of the user.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<Service>> getUserFavorites(@PathVariable Integer userId) {
        return ResponseEntity.ok(favoriteRepository.findAllByUserId(userId).stream().map(Favorite::getService).toList());
    }

    /**
     * Checks if service is favorited by user.
     * @param userId   id of the user.
     * @param serviceId id of the service.
     * @return true if favorited, in other case false.
     */
    @GetMapping("/check/{userId}/{serviceId}")
    public ResponseEntity<Boolean> checkFavorite(@PathVariable Integer userId, @PathVariable Integer serviceId) {
        return ResponseEntity.ok(favoriteRepository.existsByUserIdAndServiceId(userId, serviceId));
    }
}