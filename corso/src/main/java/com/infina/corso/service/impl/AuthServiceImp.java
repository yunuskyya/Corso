package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.CredentialsRequest;
import com.infina.corso.dto.response.AuthResponse;
import com.infina.corso.dto.response.GetUserByEmailResponse;
import com.infina.corso.model.Token;
import com.infina.corso.model.User;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.AuthService;
import com.infina.corso.service.MailService;
import com.infina.corso.service.TokenService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImp implements AuthService {

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapperConfig modelMapperConfig;
    private final UserRepository userRepository;
    private final MailService emailService;
    private static final int MAX_ATTEMPTS = 5;

    @Autowired
    public AuthServiceImp(TokenService tokenService, PasswordEncoder passwordEncoder,
                          ModelMapperConfig modelMapperConfig, UserRepository userRepository,
                          MailService emailService) {
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapperConfig = modelMapperConfig;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    private static final Logger logger = LogManager.getLogger(AuthServiceImp.class);

    @Override
    @Transactional
    public AuthResponse authenticate(CredentialsRequest credentials) {
        User inDB = userRepository.findByEmail(credentials.email())
                .orElseThrow(() -> {
                    logger.error("User not found: {}", credentials.email());
                    return new RuntimeException("User not found: " + credentials.email());
                });

        if (inDB.isAccountLocked()) {
            logger.error("User account is locked: {}", credentials.email());
            throw new RuntimeException("User account is locked: " + credentials.email());
        }

        if (!passwordEncoder.matches(credentials.password(), inDB.getPassword())) {
            logger.error("Invalid credentials for user: {}", credentials.email());
            inDB.setLoginAttempts(inDB.getLoginAttempts() + 1);

            if (inDB.getLoginAttempts() >= MAX_ATTEMPTS) {
                inDB.setAccountLocked(true);
                logger.error("User account locked due to too many failed attempts: {}", credentials.email());
                // Send email notification
                sendAccountLockedEmail(inDB);
            }

            userRepository.save(inDB);
            throw new RuntimeException("Invalid credentials for user: " + credentials.email());
        }

        inDB.setLoginAttempts(0);
        userRepository.save(inDB);

        logger.info("User authenticated: {}", inDB.getEmail());
        GetUserByEmailResponse userResp = modelMapperConfig.modelMapperForResponse().map(inDB, GetUserByEmailResponse.class);
        Token token = tokenService.generateToken(userResp, credentials);
        return new AuthResponse(userResp, token);
    }

    @Override
    public void logout(String authorizationHeader) {
        tokenService.logout(authorizationHeader);
    }

    private void sendAccountLockedEmail(User user) {
        String to = user.getEmail();
        String subject = "Hesabınız bloke edildi";
        String text = "Merhaba " + user.getFirstName() + ",\n\n" +
                "Hesabınız 5 başarısız giriş denemesi nedeniyle bloke edilmiştir. Lütfen daha sonra tekrar deneyiniz veya destek ekibimizle iletişime geçiniz.\n\n" +
                "Saygılarımızla,\n" +
                "Infina Corso Ekibi";
        emailService.sendSimpleMessage(to, subject, text);
    }
}
