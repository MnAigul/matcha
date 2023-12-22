package com.example.matcha.payload.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UserGeneral {
    private String id;
    private String email;
    private String name;
    private String lastname;
    private String bio;
    private String city;
    private Date birth;
    private String photo;
    private String role;

    public UserGeneral() {

    }
}
