package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.ChangePasswordRequest;
import com.infina.corso.dto.request.RegisterUserRequest;
import com.infina.corso.dto.response.GetAllUserResponse;
import com.infina.corso.model.enums.Role;
import com.infina.corso.model.User;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.MailService;
import com.infina.corso.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapperConfig mapper;
    private PasswordEncoder passwordEncoder;
    private MailService emailService;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Override
    public List<GetAllUserResponse> getAllUser() {
        List<User> brokers = userRepository.findByAuthoritiesContaining(Role.ROLE_BROKER);
        return brokers.stream()
                .map(user -> mapper.modelMapperForResponse().map(user, GetAllUserResponse.class))
                .collect(Collectors.toList());
    }
    @Override
    public void registerBroker(@Valid RegisterUserRequest registerUserRequest) {
        User newUser = mapper.modelMapperForRequest().map(registerUserRequest, User.class);
        newUser.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        newUser.setAuthorities(new HashSet<>() {{
            add(Role.ROLE_BROKER);
        }});
        newUser.setActive(true);
        userRepository.save(newUser);
        logger.info("Broker registered: {}", newUser.getUsername());
        String subject = "Kayıt Onayı";
        String text = "Merhaba " + newUser.getFirstName() + ",\n\n" +
                "Sistemimize başarıyla kayıt oldunuz.\n\n" +
                "Teşekkürler,\n" +
                "Infina Corso";
        emailService.sendSimpleMessage(newUser.getEmail(), subject, text);
    }
    @Override
    public void registerManager(@Valid RegisterUserRequest registerUserRequest) {
        User newUser = mapper.modelMapperForRequest().map(registerUserRequest, User.class);
        newUser.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        newUser.setAuthorities(new HashSet<>() {{
            add(Role.ROLE_MANAGER);
        }});
        newUser.setActive(true);
        userRepository.save(newUser);
        logger.info("Manager registered: {}", newUser.getUsername());
        String subject = "Kayıt Onayı";
        String text = "Merhaba " + newUser.getFirstName() + ",\n\n" +
                "Sistemimize başarıyla kayıt oldunuz.\n\n" +
                "Teşekkürler,\n" +
                "Infina Corso";
        emailService.sendSimpleMessage(newUser.getEmail(), subject, text);
    }
    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findByEmail(changePasswordRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Eski şifre yanlış");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }
}
