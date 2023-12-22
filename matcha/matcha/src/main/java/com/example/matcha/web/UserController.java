package com.example.matcha.web;

import com.example.matcha.entity.Role;
import com.example.matcha.entity.User;
import com.example.matcha.payload.request.IdRequest;
import com.example.matcha.payload.request.LoginRequest;
import com.example.matcha.payload.response.IUserResponse;
import com.example.matcha.payload.response.JWTTokenSuccessResponse;
import com.example.matcha.payload.response.UserGeneral;
import com.example.matcha.payload.response.UserProfile;
import com.example.matcha.repository.UserRepository;
import com.example.matcha.security.JWTAuthenticationFilter;
import com.example.matcha.security.JWTTokenProvider;
import com.example.matcha.security.SecurityConstants;
import com.example.matcha.services.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserService userService;

    @GetMapping("/getUsers")
    public ResponseEntity<Object> getUsers() {
        User[] users = userService.GetAllUsers();
        UserGeneral[] userResponses = Arrays.stream(users)
                .map(user -> new UserGeneral(Long.toString(user.getId()), user.getEmail(), user.getName(), user.getLastname(), user.getBio(), user.getCity(), user.getBirth(), user.getPhoto(), user.getRoles().get(0).getName()))
                .toArray(UserGeneral[]::new);
        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/getProfile")
    public ResponseEntity<Object> getMyUser(@RequestParam String id) {
        System.out.println(userService.getUserProfile(Long.parseLong(id)));
        return ResponseEntity.ok(userService.getUserProfile(Long.parseLong(id)));
    }

    @PostMapping("/updateProfile/{id}")
    public ResponseEntity<Object> updateMyUser(@PathVariable String id, @RequestBody UserProfile userProfile) {
        User old = userService.getOne(Long.valueOf(id));
        old.setName(userProfile.getName());
        old.setLastname(userProfile.getLastname());
        old.setBirth(userProfile.getBirth());
        old.setCity(userProfile.getCity());
        old.setBio(userProfile.getBio());
        userService.save(old);
        return ResponseEntity.ok(userService.getUserProfile(Long.parseLong(id)));
    }

    @PostMapping("/updateRole/{id}")
    public ResponseEntity<User> updateRole(@PathVariable String id, @RequestBody String role) {
        if (role.startsWith("ROLE_USER"))
            role = "ROLE_USER";
        else
            role = "ROLE_ADMIN";
        System.out.println("%" + role + "%");
        userService.updateRole(Long.valueOf(id), role);
        return ResponseEntity.ok(userService.getOne(Long.valueOf(id)));
    }


}
