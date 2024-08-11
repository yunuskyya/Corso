package com.infina.corso.controller;

import com.infina.corso.dto.request.CredentialsRequest;
import com.infina.corso.dto.response.AuthResponse;
import com.infina.corso.model.User;
import com.infina.corso.model.enums.Role;
import com.infina.corso.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Operations related to user authentication")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    @Operation(summary = "Authenticate user", description = "Authenticate a user with the given credentials.")
    public ResponseEntity<AuthResponse> handleAuthentication(@Valid @RequestBody CredentialsRequest credentials) {
        AuthResponse authResponse = authService.authenticate(credentials);
        ResponseCookie cookie = ResponseCookie.from("corso-token", authResponse.getToken().getTokenId())
                .path("/").sameSite("None").secure(true).httpOnly(true).build();
        authResponse.setToken(null);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(authResponse);
    }
    @DeleteMapping
    @Operation(summary = "Logout user", description = "Logout the currently authenticated user.")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "corso-token", required = false) String cookieValue) {
        try {
            if (cookieValue != null && !cookieValue.isEmpty()) {
                var tokenWithPrefix = "CorsoToken " + cookieValue;
                authService.logout(tokenWithPrefix);
            }
            ResponseCookie cookie = ResponseCookie.from("corso-token", "")
                    .path("/").httpOnly(true).maxAge(0).build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/currentUser")
    @Operation(summary = "Get current user", description = "Retrieve the currently authenticated user.")
    public ResponseEntity<User> getCurrentUser(@RequestParam(value = "role", defaultValue = "broker") String role) {
        return ResponseEntity.ok(
                User.builder()
                        .id(1)
                        .authorities(Set.of(
                                switch (role) {
                                    case "admin" -> Role.ROLE_ADMIN;
                                    case "manager" -> Role.ROLE_MANAGER;
                                    default -> Role.ROLE_BROKER;
                                }
                        ))
                        .build()
        );
    }

}
