package com.infina.corso.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infina.corso.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private boolean active;
    private boolean isDeleted;
    private String resetPasswordToken;
    private String activationToken;
    private int loginAttempts;
    private boolean accountLocked;
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @JsonIgnore
    private List<Customer> customerList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Token> tokens;

    @OneToMany(mappedBy = "user" , fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Transaction> transactions;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Role> authorities;

}
