package com.example.matcha.payload.request;

import com.example.matcha.annotations.PasswordMatches;
import com.example.matcha.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@PasswordMatches
public class SignupRequest {

    @Email(message = "It schould have email format")
    @NotBlank(message = "User Email is required")
    @ValidEmail
    private String email;

    @NotEmpty(message = "Please enter your firstname")
    private String firstname;

    @NotEmpty(message = "Please enter your lastname")
    private String lastname;

    @NotEmpty(message = "Please enter your username")
    private String username;

    @NotEmpty(message = "Password is required")
    @Size(min = 6)
    private String password;

    private String confirmPassword;
}
