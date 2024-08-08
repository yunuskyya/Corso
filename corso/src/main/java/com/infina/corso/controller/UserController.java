package com.infina.corso.controller;

import com.infina.corso.config.CurrentUser;
import com.infina.corso.dto.request.ChangePasswordRequest;
import com.infina.corso.dto.request.RegisterUserRequest;
import com.infina.corso.dto.response.GetAllUserResponse;
import com.infina.corso.service.MailService;
import com.infina.corso.service.UserService;
import com.infina.corso.shared.GenericMessage;
import com.infina.corso.shared.Messages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {

    private final UserService userService;
    private final MailService emailService;

    public UserController(UserService userService, MailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/brokers")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_MANAGER')")
    @Operation(summary = "Get all brokers", description = "Retrieve a list of all brokers.")
    public ResponseEntity<List<GetAllUserResponse>> getAllBrokers() {
        List<GetAllUserResponse> brokers = userService.getAllUser();
        return ResponseEntity.ok(brokers);
    }

    @PostMapping("/register/broker")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_MANAGER')")
    @Operation(summary = "Register a new broker", description = "Register a new broker with the given details.")
    public GenericMessage registerUser(@Valid @RequestBody RegisterUserRequest request) {
        userService.registerBroker(request);
        return new GenericMessage(Messages.getMessageForLocale("corso.register.user.success.message.successfully",
                LocaleContextHolder.getLocale()));
    }

    @PostMapping("/register/manager")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Register a new manager", description = "Register a new manager with the given details.")
    public GenericMessage registerManager(@Valid @RequestBody RegisterUserRequest request) {
        userService.registerManager(request);
        return new GenericMessage(Messages.getMessageForLocale("corso.register.user.success.message.successfully",
                LocaleContextHolder.getLocale()));
    }

    @GetMapping("/role")
    @Operation(summary = "Get user role", description = "Retrieve the role of the currently authenticated user.")
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
    @Operation(summary = "Send test email", description = "Send a test email to a predefined address.")
    public String sendEmail() {
        String to = "nhtyl07@gmail.com";
        String subject = "Test Email";
        String text = "This is a test email sent from Spring Boot application.";
        emailService.sendSimpleMessage(to, subject, text);
        return "Email sent successfully!";
    }

    @PutMapping("/change-password")
    @Operation(summary = "Change user password", description = "Change the password of a user.")
    public GenericMessage changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            userService.changePassword(changePasswordRequest);
            return  new GenericMessage(Messages.getMessageForLocale("corso.change.password.success.message.successfully",
                    LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            return new GenericMessage(Messages.getMessageForLocale("corso.change.password.error.message.error",
                    LocaleContextHolder.getLocale()));
        }
    }

    @PutMapping("/activate")
    @PreAuthorize("hasRole('ROLE_MANAGER') OR hasRole('ROLE_ADMIN')")
    @Operation(summary = "Activate a user account by email", description = "Activate a user account that is currently locked using their email.")
    public GenericMessage activateUserByEmail(@RequestParam String email) {
        try {
            userService.activateUserByEmail(email);
            return new GenericMessage(Messages.getMessageForLocale("corso.activate.user.success.message.successfully",
                    LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            return new GenericMessage(Messages.getMessageForLocale("corso.activate.user.error.message.error",
                    LocaleContextHolder.getLocale()));
        }
    }
}
