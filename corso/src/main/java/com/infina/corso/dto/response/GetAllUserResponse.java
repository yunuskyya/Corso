package com.infina.corso.dto.response;
import com.infina.corso.model.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

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
