package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.RegisterUserRequest;
import com.infina.corso.model.enums.Role;
import com.infina.corso.model.User;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapperConfig mapper;
    private PasswordEncoder passwordEncoder;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);


    @Override
    @Transactional
    public void registerUser(RegisterUserRequest registerUserRequest) {
        User newUser = mapper.modelMapperForRequest().map(registerUserRequest, User.class);
        newUser.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        newUser.setAuthorities(new HashSet<>() {{
            add(Role.ROLE_BROKER);
        }});
        newUser.setActive(true);
        userRepository.save(newUser);
        logger.info("User registered: {}", newUser.getUsername());
    }



}
