package com.mostafahassan.thunder.Moduel;

import java.util.Map;

public class User  {

              private   String username,email,country,status,gender,relationshi_pstatus,profileImage;

    public User(String username, String email, String country, String status, String gender, String relationshi_pstatus, String profileImage) {
        this.username = username;
        this.email = email;
        this.country = country;
        this.status = status;
        this.gender = gender;
        this.relationshi_pstatus = relationshi_pstatus;
        this.profileImage = profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelationshi_pstatus() {
        return relationshi_pstatus;
    }

    public void setRelationshi_pstatus(String relationshi_pstatus) {
        this.relationshi_pstatus = relationshi_pstatus;
    }
}
