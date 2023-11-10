package com.example.matcha.services;

import com.example.matcha.entity.Role;
import com.example.matcha.entity.User;
import com.example.matcha.payload.request.SignupRequest;
import com.example.matcha.repository.RoleRepository;
import com.example.matcha.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.setRoles(roles);


        try {
            LOG.info("Saving User {}", userIn.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            LOG.error("Error during registration, {}", e.getMessage());
            throw new UserExistsException("The user " + user.getUsername() + " already exists. Please check credentials");
        }
    }
}
