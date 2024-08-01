package com.infina.corso.controller;



import com.infina.corso.dto.request.RegisterUserRequest;
import com.infina.corso.dto.response.GetAllUserResponse;
import com.infina.corso.service.UserService;



import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

}
