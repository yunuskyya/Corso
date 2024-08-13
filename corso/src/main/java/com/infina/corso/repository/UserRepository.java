package com.infina.corso.repository;

import com.infina.corso.model.User;
import com.infina.corso.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);;
    Optional<User> findByResetPasswordToken(String token);
    Page<User> findAllByAuthorities(Role role, Pageable pageable);

}
