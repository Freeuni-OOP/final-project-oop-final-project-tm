package com.finalproject.backend.modules.services;


import com.finalproject.backend.entities.Service;
import com.finalproject.backend.repositories.FavoriteRepository;
import com.finalproject.backend.repositories.UserModuleRepository;
import com.finalproject.backend.repositories.listingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @PostMapping("/{userId}/{serviceId}")
    @Transactional
    public ResponseEntity<String> toggleFavorite(@PathVariable Integer userId, @PathVariable Integer serviceId) {
        if (favoriteRepository.existsByUserIdAndServiceId(userId, serviceId)) {
            favoriteRepository.deleteByUserIdAndServiceId(userId, serviceId);
            return ResponseEntity.ok("Removed");
        }
        Favorite fav = new Favorite();
        fav.setUser(userRepository.findById(userId).orElseThrow());
        fav.setService(listingRepository.findById(serviceId).orElseThrow());
        favoriteRepository.save(fav);

        return ResponseEntity.ok("Added");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Service>> getUserFavorites(@PathVariable Integer userId) {
        return ResponseEntity.ok(favoriteRepository.findAllByUserId(userId).stream().map(Favorite::getService).toList());
    }

    @GetMapping("/check/{userId}/{serviceId}")
    public ResponseEntity<Boolean> checkFavorite(@PathVariable Integer userId, @PathVariable Integer serviceId) {
        return ResponseEntity.ok(favoriteRepository.existsByUserIdAndServiceId(userId, serviceId));
    }
}