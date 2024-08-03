package com.infina.corso.controller;

import com.infina.corso.config.CurrentUser;
import com.infina.corso.dto.request.ChangePasswordRequest;
import com.infina.corso.service.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infina.corso.dto.request.RegisterUserRequest;
import com.infina.corso.dto.response.GetAllUserResponse;
import com.infina.corso.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final MailService emailService;

    public UserController(UserService userService, MailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/brokers")
    public ResponseEntity<List<GetAllUserResponse>> getAllBrokers() {
        List<GetAllUserResponse> brokers = userService.getAllUser();
        return ResponseEntity.ok(brokers);
    }

    @PostMapping("/register/broker")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void registerBroker(@RequestBody RegisterUserRequest request) {
        userService.registerBroker(request);
    }

    @PostMapping("/register/manager")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void registerManager(@RequestBody RegisterUserRequest request) {
        userService.registerManager(request);
    }

    @GetMapping("/role")
    public ResponseEntity<?> getUserRole(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication is required");
        }

        CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
        Map<String, Object> response = new HashMap<>();
        response.put("id", currentUser.getId());
        response.put("authorities", currentUser.getAuthorities());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/send")
    public String sendEmail() {
        String to = "nhtyl07@gmail.com";
        String subject = "Test Email";
        String text = "This is a test email sent from Spring Boot application.";
        emailService.sendSimpleMessage(to, subject, text);
        return "Email sent successfully!";
    }
    @PutMapping("/change-password")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            userService.changePassword(changePasswordRequest);
            return ResponseEntity.ok("Şifreniz başarıyla güncellendi.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

