package com.infina.corso.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetAllUserResponse {
    private int id;
    private LocalDateTime createdAt;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean isDeleted;
    private boolean accountLocked;
    private LocalDateTime updatedAt;
}
