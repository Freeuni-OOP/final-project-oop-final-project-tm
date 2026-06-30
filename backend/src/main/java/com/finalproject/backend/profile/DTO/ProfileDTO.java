package com.finalproject.backend.profile.DTO;

public class ProfileDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String aboutMe;
    private String imagePath;

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getImagePath() {
        return imagePath;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ProfileDTO(Integer id, String firstName, String lastName, String email, String aboutMe, String imagePath) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.aboutMe = aboutMe;
        this.imagePath = imagePath;
    }
}