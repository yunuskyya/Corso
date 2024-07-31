package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.CredentialsRequest;
import com.infina.corso.dto.response.AuthResponse;
import com.infina.corso.dto.response.GetUserByEmailResponse;
import com.infina.corso.model.Token;
import com.infina.corso.model.User;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.AuthService;
import com.infina.corso.service.TokenService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class AuthServiceImp implements AuthService {

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapperConfig modelMapperConfig;
    private final UserRepository userRepository;


    @Autowired
    public AuthServiceImp(TokenService tokenService, PasswordEncoder passwordEncoder,
                          ModelMapperConfig modelMapperConfig, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapperConfig = modelMapperConfig;
        this.userRepository = userRepository;

    }

    private static final Logger logger = LogManager.getLogger(AuthServiceImp.class);

    @Override
    public AuthResponse authenticate(CredentialsRequest credentials) {
        User inDB = userRepository.findByEmail(credentials.email())
                .orElseGet(() -> userRepository.findByUsername(credentials.username())
                        .orElseThrow(() -> {
                            logger.error("User not found: {}",
                                    credentials.email() != null ? credentials.email() : credentials.username());
                            return new RuntimeException("User not found: " +
                                    (credentials.email() != null ? credentials.email() : credentials.username()));
                        }));

        if (!passwordEncoder.matches(credentials.password(), inDB.getPassword())) {
            logger.error("Invalid credentials for user: {}", credentials.email() != null ? credentials.email() : credentials.username());
            throw new RuntimeException("Invalid credentials for user: " +
                    (credentials.email() != null ? credentials.email() : credentials.username()));
        } else if (!inDB.isActive() || inDB.isDeleted()) {
            logger.error("User is not active or deleted: {}", credentials.email() != null ? credentials.email() : credentials.username());
            throw new RuntimeException("User is not active or deleted: " +
                    (credentials.email() != null ? credentials.email() : credentials.username()));
        }

        logger.info("User authenticated: {}", inDB.getEmail());
        GetUserByEmailResponse userResp = modelMapperConfig.modelMapperForResponse().map(inDB, GetUserByEmailResponse.class);
        Token token = tokenService.generateToken(userResp, credentials);
        return new AuthResponse(userResp, token);
    }


    @Override
    public void logout(String authorizationHeader) {
        tokenService.logout(authorizationHeader);
    }
}
