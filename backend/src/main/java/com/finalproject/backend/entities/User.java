package com.finalproject.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email",
    unique = true)
    private String email;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "password_hash")
    private String passHash;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "is_enabled", columnDefinition = "boolean default false")
    private Boolean isEnabled;

    @Column(name = "verification_code")
    private String verificationCode;

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

    public String getPassHash() { return passHash; }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
