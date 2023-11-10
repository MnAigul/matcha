package com.example.matcha.validations;

import com.example.matcha.annotations.PasswordMatches;
import com.example.matcha.annotations.ValidEmail;
import com.example.matcha.payload.request.SignupRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator  implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest userSignupRequest = (SignupRequest) obj;
        return userSignupRequest.getPassword().equals(userSignupRequest.getConfirmPassword());
    }
}
