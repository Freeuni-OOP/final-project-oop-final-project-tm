package com.finalproject.backend.modules.services;

import com.finalproject.backend.modules.users.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final listingRepository listingRepository;

    public FavoriteController(FavoriteRepository favRep, UserRepository userRep, listingRepository listRep) {
        this.favoriteRepository = favRep;
        this.userRepository = userRep;
        this.listingRepository = listRep;
    }

    @PostMapping("/{userId}/{serviceId}")
    @Transactional
    public ResponseEntity<String> toggleFavorite(@PathVariable Long userId, @PathVariable Integer serviceId) {
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
    public ResponseEntity<List<listing>> getUserFavorites(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteRepository.findAllByUserId(userId).stream().map(Favorite::getService).toList());
    }

    @GetMapping("/check/{userId}/{serviceId}")
    public ResponseEntity<Boolean> checkFavorite(@PathVariable Long userId, @PathVariable Integer serviceId) {
        return ResponseEntity.ok(favoriteRepository.existsByUserIdAndServiceId(userId, serviceId));
    }
}