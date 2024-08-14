package com.infina.corso.service.impl;

import com.infina.corso.config.CurrentUser;
import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.CredentialsRequest;
import com.infina.corso.dto.response.AuthResponse;
import com.infina.corso.dto.response.GetUserByEmailResponse;
import com.infina.corso.exception.AuthenticationException;
import com.infina.corso.exception.GeneralErrorException;
import com.infina.corso.model.Token;
import com.infina.corso.model.User;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.AuthService;
import com.infina.corso.service.TokenService;
import com.infina.corso.util.EmailHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService {

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapperConfig modelMapperConfig;
    private final UserRepository userRepository;
    private final EmailHelper emailHelper;
    private static final int MAX_ATTEMPTS = 5;

    @Autowired
    public AuthServiceImp(TokenService tokenService, PasswordEncoder passwordEncoder,
                          ModelMapperConfig modelMapperConfig, UserRepository userRepository,
                          EmailHelper emailHelper) {
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapperConfig = modelMapperConfig;
        this.userRepository = userRepository;
        this.emailHelper = emailHelper;

    }

    private static final Logger logger = LogManager.getLogger(AuthServiceImp.class);

    @Override
    public AuthResponse authenticate(CredentialsRequest credentials) {
        User inDB = userRepository.findByEmail(credentials.email())
                .orElseThrow(() -> {
                    logger.error("User not found: {}", credentials.email());
                    return new AuthenticationException();
                });

        if (inDB.isAccountLocked()) {
            logger.error("User account is locked: {}", credentials.email());
            throw new GeneralErrorException("corso.user.locked.error.message");
        }
        if (inDB.isDeleted()) {
            logger.error("User account is deleted: {}", credentials.email());
            throw new GeneralErrorException("corso.user.deleted.error.message");
        }

        if (!passwordEncoder.matches(credentials.password(), inDB.getPassword())) {
            logger.error("Invalid credentials for user: {}", credentials.email());
            inDB.setLoginAttempts(inDB.getLoginAttempts() + 1);
            logger.info("Login attempts: {}", inDB.getLoginAttempts());

            if (inDB.getLoginAttempts() >= MAX_ATTEMPTS) {
                inDB.setAccountLocked(true);
                logger.error("User account locked due to too many failed attempts: {}", credentials.email());
                // Send email notification
                emailHelper.sendAccountLockedEmail(inDB);
            }
            userRepository.save(inDB);
            throw new AuthenticationException();
        }

        inDB.setLoginAttempts(0);
        userRepository.save(inDB);

        logger.info("User authenticated: {}", inDB.getEmail());
        GetUserByEmailResponse userResp = modelMapperConfig.modelMapperForResponse().map(inDB,
                GetUserByEmailResponse.class);
        Token token = tokenService.generateToken(inDB);
        return new AuthResponse(userResp, token);
    }

    @Override
    public void logout(String authorizationHeader) {
        tokenService.logout(authorizationHeader);
    }



    @Override
    public int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int id = ((CurrentUser) authentication.getPrincipal()).getId();
        logger.info("Current user id: {}", id);
        return id;
    }
}
