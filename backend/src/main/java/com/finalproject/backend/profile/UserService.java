package com.finalproject.backend.profile;

import com.finalproject.backend.profile.DTO.ProfileDTO;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private  final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Returns ProfileDTO object by getting data from Users table by id
    public ProfileDTO getUser(Integer id) {
        User userEntity = userRepository.findById(id).orElse(null);
        if(userEntity == null) return null;
        // everything except password
        return new ProfileDTO(id, userEntity.getFirstName(), userEntity.getLastName(), userEntity.getEmail(), userEntity.getAboutMe(),userEntity.getImagePath());
    }

    // Updates Profile (firstName, lastName, AboutMe, Image)
    public void UpdatePublicUser(ProfileDTO profileDTO) {
        User user = userRepository.findById(profileDTO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.setFirstName(profileDTO.getFirstName());
        user.setLastName(profileDTO.getLastName());
        System.out.println("here");
        System.out.println(profileDTO.getFirstName());
        user.setAboutMe(profileDTO.getAboutMe());

        userRepository.save(user);
    }

    public void updateProfilePicture(Integer id, String imagePath) {
        User user = userRepository.findById(id).orElseThrow();

        user.setImagePath(imagePath);
        userRepository.save(user);
    }

    public Integer getIdByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not Found"));
        return user.getId();
    }
}