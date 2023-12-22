package com.example.matcha.payload.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IUserResponse {
    Long id;
    String email;
}
