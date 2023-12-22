package com.example.matcha.services;

import com.example.matcha.entity.Role;
import com.example.matcha.entity.User;
import com.example.matcha.payload.request.SignupRequest;
import com.example.matcha.payload.response.UserProfile;
import com.example.matcha.repository.RoleRepository;
import com.example.matcha.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SignupRequest userIn) {
        User user = new User();
        List<Role> roles = user.getRoles();

        if (roles == null) {
            roles = new ArrayList<>();
        }
        Role roleUser = roleRepository.findByName("ROLE_USER");
        roles.add(roleUser);

        user.setEmail(userIn.getEmail());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.setRoles(roles);
        user.setLastTimeVisit(LocalDateTime.now());


        try {
            LOG.info("Saving User {}", userIn.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            LOG.error("Error during registration, {}", e.getMessage());
            throw new UserExistsException("The user " + user.getUsername() + " already exists. Please check credentials");
        }
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findUserById(id);
        if (user != null) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("User with this id not exists in bd!");
        }
    }


    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findUserByEmail(username);
        if (user != null) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("User with this id not exists in bd!");
        }
    }

    public User[] GetAllUsers() {
        User[] users = userRepository.findAll().toArray(new User[0]);
        return users;
    }

    public UserProfile getUserProfile(Long id) {
        User user = userRepository.getOne(id);
        UserProfile userProfile = new UserProfile(user.getName(), user.getLastname(), user.getBio(), user.getCity(), user.getBirth(), user.getPhoto());
        return userProfile;
    }


    public void save(User user) {
        userRepository.save(user);
    }

    public User getOne(Long valueOf) {
        return userRepository.getOne(valueOf);
    }

    public User updatePhoto(Long id, String fileName) {
        User user = getOne(Long.valueOf(id));
        user.setPhoto(fileName);
        this.save(user);
        return userRepository.getOne(id);
    }

    public User updateRole(Long id, String role) {
        User user = getOne(Long.valueOf(id));
        System.out.println("yatut");
        System.out.println(role);
        System.out.println("yatut");
        List<Role> roles = user.getRoles();
        roles.clear();
        roles.add(0, roleRepository.findByName(role));
        user.setRoles(roles);
        this.save(user);
        return userRepository.getOne(id);
    }
}
