package com.example.matcha.web;

import com.example.matcha.entity.BlackList;
import com.example.matcha.entity.Role;
import com.example.matcha.entity.User;
import com.example.matcha.payload.request.LoginRequest;
import com.example.matcha.payload.request.SignupRequest;
import com.example.matcha.payload.response.JWTTokenSuccessResponse;
import com.example.matcha.payload.response.MessageResponse;
import com.example.matcha.security.JWTAuthenticationFilter;
import com.example.matcha.security.JWTTokenProvider;
import com.example.matcha.security.SecurityConstants;
import com.example.matcha.services.BlackListService;
import com.example.matcha.services.CustomUserDetailsService;
import com.example.matcha.services.UserService;
import com.example.matcha.validations.ResponseErrorValidation;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.matcha.security.SecurityConstants.EXPIRATION_TIME;
import static com.example.matcha.security.SecurityConstants.EXPIRATION_TIME_REFRESH;
import static org.apache.commons.lang3.stream.Streams.stream;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private UserService userService;
    @Autowired
    private BlackListService blackListService;



    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)){
            return errors;
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));
        User user = userService.getUserByUsername(loginRequest.getEmail());
        String access_token = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(user, EXPIRATION_TIME, "ACCESS");
        String refresh_token = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(user, EXPIRATION_TIME_REFRESH, "REFRESH");

        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, access_token, refresh_token, EXPIRATION_TIME, EXPIRATION_TIME_REFRESH, user.getRoles().get(0).getName(), user.getId().toString()));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(SecurityConstants.HEADER_STRING);
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            String refresh_token =  jwtAuthenticationFilter.getJWTFromRequest(request);
            Long userId = jwtTokenProvider.getUserIdFromToken(refresh_token);
            if (!jwtTokenProvider.validateToken(refresh_token, "REFRESH") || blackListService.searchToken(userId, refresh_token)) {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is invalid");
            } else {
                User user = customUserDetailsService.loadUserById(userId);
                String access_token = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(user, EXPIRATION_TIME, "ACCESS");
                String new_refresh_token = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(user, EXPIRATION_TIME_REFRESH, "REFRESH");
                blackListService.saveToken(userId, refresh_token);
                return ResponseEntity.ok(new JWTTokenSuccessResponse(true, access_token, new_refresh_token, EXPIRATION_TIME, EXPIRATION_TIME_REFRESH, user.getRoles().get(0).getName(), userId.toString()));
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        userService.createUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response) {
       // String authorizationHeader = request.getHeader(SecurityConstants.HEADER_STRING);
        String refresh_token =  jwtAuthenticationFilter.getJWTFromRequest(request);
        Long userId = jwtTokenProvider.getUserIdFromToken(refresh_token);
        blackListService.saveToken(userId, refresh_token);
        return ResponseEntity.status(HttpStatus.OK).body("Logout successful");
    }

}
