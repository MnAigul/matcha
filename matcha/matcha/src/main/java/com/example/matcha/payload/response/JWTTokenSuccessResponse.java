package com.example.matcha.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JWTTokenSuccessResponse {
    private boolean success;
    private String access_token;
    private String refresh_token;
    private long access_expires;
    private long refresh_expires;
    private String role;
    private String id;


}
