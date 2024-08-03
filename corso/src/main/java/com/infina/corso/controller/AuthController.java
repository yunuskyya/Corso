package com.infina.corso.controller;

import com.infina.corso.dto.request.CredentialsRequest;
import com.infina.corso.dto.response.AuthResponse;
import com.infina.corso.model.User;
import com.infina.corso.model.enums.Role;
import com.infina.corso.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> handleAuthentication(@Valid @RequestBody CredentialsRequest credentials) {
        try {
            AuthResponse authResponse = authService.authenticate(credentials);
            ResponseCookie cookie = ResponseCookie.from("corso-token", authResponse.getToken().getTokenId())
                    .path("/").sameSite("None").secure(true).httpOnly(true).build();
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping
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
