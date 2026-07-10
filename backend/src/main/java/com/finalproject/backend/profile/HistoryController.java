package com.finalproject.backend.profile;

import com.finalproject.backend.profile.DTO.ServiceDTO;
import com.finalproject.backend.services.HistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile/services")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/offered")
    public ResponseEntity<List<ServiceDTO>> getOfferedServices(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null){
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(historyService.getOfferedServices(userId));
    }

    @GetMapping("/registered")
    public ResponseEntity<List<ServiceDTO>> getRegisteredServices(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null){
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(historyService.getRegisteredServices(userId));
    }
}