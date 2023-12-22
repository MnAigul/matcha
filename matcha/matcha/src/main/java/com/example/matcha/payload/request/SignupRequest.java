package com.example.matcha.payload.request;

import com.example.matcha.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class SignupRequest {

    @Email(message = "It schould have email format")
    @NotBlank(message = "User Email is required")
    @ValidEmail
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 6)
    private String password;

}
