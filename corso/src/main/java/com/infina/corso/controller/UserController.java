package com.infina.corso.controller;

import com.infina.corso.config.CurrentUser;
import com.infina.corso.dto.request.*;
import com.infina.corso.dto.response.GetAllUserResponse;
import com.infina.corso.service.MailService;
import com.infina.corso.service.UserService;
import com.infina.corso.shared.GenericMessage;
import com.infina.corso.shared.Messages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    @Operation(summary = "Get all brokers", description = "Retrieve a paginated list of all brokers.")
    public ResponseEntity<Page<GetAllUserResponse>> getAllBrokers(Pageable pageable) {
        Page<GetAllUserResponse> brokers = userService.getAllUser(pageable);
        return ResponseEntity.ok(brokers);
    }

    @PostMapping("/register/broker")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_MANAGER')")
    @Operation(summary = "Register a new broker", description = "Register a new broker with the given details.")
    public GenericMessage registerBroker(@Valid @RequestBody RegisterUserRequest request) {
        userService.registerBroker(request);
        return new GenericMessage(Messages.getMessageForLocale("corso.register.user.success.message.successfully",
                LocaleContextHolder.getLocale()));
    }

    @PostMapping("/register/manager")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Register a new manager", description = "Register a new manager with the given details.")
    public GenericMessage registerManager(@Valid @RequestBody RegisterManagerRequest request) {
        userService.registerManager(request);
        return new GenericMessage(Messages.getMessageForLocale("corso.register.user.success.message.successfully",
                LocaleContextHolder.getLocale()));
    }

    @GetMapping("/role")
    @Operation(summary = "Get user role", description = "Retrieve the role of the currently authenticated user.")
    public ResponseEntity<Map<String, Object>> getUserRole(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
        Map<String, Object> response = new HashMap<>();
        response.put("id", currentUser.getId());

        String role = currentUser.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new RuntimeException("Not found role for the user"));
        response.put("role", role);

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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete a user", description = "Delete a user by ID.")
    public GenericMessage deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return new GenericMessage(Messages.getMessageForLocale("corso.delete.user.success.message.successfully",
                LocaleContextHolder.getLocale()));
    }
    @PutMapping("/set-password")
    @Operation(summary = "Set user password", description = "Set the password for a user who registered but didn't set a password yet.")
    public GenericMessage setPassword(@RequestParam("token") String token, @Valid @RequestBody UpdatePasswordRequest newPassword) {
        try {
            userService.updatePassword(token, newPassword);
            return new GenericMessage("Şifre başarıyla oluşturuldu.");
        } catch (RuntimeException e) {
            return new GenericMessage("Şifre oluşturulurken bir hata oluştu: " + e.getMessage());
        }
    }
    @PutMapping("/unBlock")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_MANAGER')")
    @Operation(summary = "Unblock a user", description = "Unblock a user by email.")
    public GenericMessage unBlockUser(@Valid @RequestBody UserUnblockRequest userUnblockRequest) {
        userService.userUnblock(userUnblockRequest);
        return new GenericMessage(Messages.getMessageForLocale("corso.unblock.user.success.message.successfully",
                LocaleContextHolder.getLocale()));
    }
    @PutMapping("/activate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Activate a user", description = "Activate a user by email.")
    public GenericMessage activateUser(@Valid @RequestBody UserActivateRequest userActivateRequest) {
        userService.activateUser(userActivateRequest);
        return new GenericMessage(Messages.getMessageForLocale("corso.activate.user.success.message.successfully",
                LocaleContextHolder.getLocale()));
    }
}
