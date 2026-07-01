package com.finalproject.backend.servicefeature.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/service")
@CrossOrigin(origins = "http://localhost:5173")
public class PostServiceTitleController {

    public static class ServiceRequest {
        private String titleString;
        private String description;
        private String userId;

        public String getTitleString(){
            return titleString;
        }

        public String getDescription(){
            return description;
        }

        public String getUserId(){
            return userId;
        }

        public void setTitleString(String title){
            this.titleString = title;
        }

        public void setDescription(String description){
            this.description = description;
        }

        public void setUserId(String userId){
            this.userId = userId;
        }
    }

    @PostMapping("/information")
    public ResponseEntity<String> postServiceInformation(@RequestBody ServiceRequest serviceInformation) {
        String title = serviceInformation.getTitleString();
        String description = serviceInformation.getDescription();

        if(description.isEmpty() || title.isEmpty()){
            return ResponseEntity.status(400).body("Title and Description cannot be empty");
        }

        if(title.length() > 100 || description.length() > 1000){
            return ResponseEntity.status(400).body("Max character limit acceded");
        }

        // Dao here for pushing those information into database creating new service

        //System.out.println("Title: " + title + "\n" + "Description: " + description);

        return ResponseEntity.ok("Service information posted");


    }
}
