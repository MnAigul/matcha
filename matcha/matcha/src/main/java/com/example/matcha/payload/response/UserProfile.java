package com.example.matcha.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UserProfile {
    private String name;
    private String lastname;
    private String bio;
    private String city;
    private Date birth;
    private String photo;

    public UserProfile() {

    }

}

