package com.finalproject.backend.profile.history;


import com.finalproject.backend.profile.DTO.ServiceDTO;
import com.finalproject.backend.services.CookieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile/services/")
public class TakenServicesController {
    private TakenServicesService takenServicesService;
    private CookieService cookieService;

    public TakenServicesController(TakenServicesService takenServicesService, CookieService cookieService) {
        this.takenServicesService = takenServicesService;
        this.cookieService = cookieService;
    }

    @GetMapping("offered/{id}")
    public ResponseEntity<List<ServiceDTO>> getOfferedServices(@PathVariable Integer id) {
        return ResponseEntity.ok(takenServicesService.getOfferedServices(id));
    }

    @GetMapping("registered")
    public ResponseEntity<List<ServiceDTO>> getRegisteredServices(@CookieValue(value = "jwt_token", required = false) String userCookie) {
        int id = cookieService.checkCookie(userCookie);
        return ResponseEntity.ok(takenServicesService.getRegisteredServices(id));
    }
}
