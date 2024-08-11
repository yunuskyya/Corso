package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.*;
import com.infina.corso.dto.response.GetAllUserResponse;
import com.infina.corso.exception.AccessDeniedException;
import com.infina.corso.exception.UserNotFoundException;
import com.infina.corso.model.Transaction;
import com.infina.corso.model.User;
import com.infina.corso.model.enums.Role;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.TokenService;
import com.infina.corso.service.UserService;
import com.infina.corso.util.EmailHelper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final EmailHelper emailHelper;
    private UserRepository userRepository;
    private ModelMapperConfig mapper;
    private PasswordEncoder passwordEncoder;
    private TokenService tokenService;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Override
    public Page<GetAllUserResponse> getAllUser(Pageable pageable) {
        Page<User> brokers = userRepository.findByAuthoritiesContaining(Role.ROLE_BROKER, pageable);
        return brokers.map(user -> mapper.modelMapperForResponse().map(user, GetAllUserResponse.class));
    }
    @Override
    public void registerBroker(@Valid RegisterUserRequest registerUserRequest) {
        User newUser = mapper.modelMapperForRequest().map(registerUserRequest, User.class);
        newUser.setPassword(passwordEncoder.encode(""));
        newUser.setAuthorities(new HashSet<>() {{
            add(Role.ROLE_BROKER);
        }});
        newUser.setActive(true);
        newUser.setResetPasswordToken(UUID.randomUUID().toString());// Bu eklendikten sonra login olunca şifre değiştirme ekranı gelecek
        userRepository.save(newUser);
        logger.info("Broker registered: {}", newUser.getUsername());
        emailHelper.sendTokenEmail(newUser.getEmail(), newUser.getResetPasswordToken());
    }

    @Override
    public void registerManager(@Valid RegisterManagerRequest request) {
        User newUser = mapper.modelMapperForRequest().map(request, User.class);
        String rawPassword = request.getPassword();  // Ham şifreyi saklayın
        newUser.setPassword(passwordEncoder.encode(rawPassword));
        newUser.setAuthorities(new HashSet<>() {{
            add(Role.ROLE_MANAGER);
        }});
        newUser.setActive(true);
        userRepository.save(newUser);
        logger.info("Manager registered: {}", newUser.getEmail());
        emailHelper.sendRegistrationEmail(newUser);

    }
    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findByEmail(changePasswordRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Eski şifre yanlış");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    public List<Transaction> getAllTransactionsById(int id){
        Optional<User> user = userRepository.findById(id);
        return user.get().getTransactions();
    }
    @Override
    public void deleteUser(int id){
      User userInDB = userRepository.findById(id).orElseThrow(() -> {
          logger.error("User not found with id: {}", id);
          throw new UserNotFoundException("User not found with id: " + id);
      });
      userInDB.setDeleted(true);
        userRepository.save(userInDB);
        logger.info("User deleted: {}", userInDB.getUsername());
    }




    @Override
    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        User userInDb = userRepository.findByEmail(passwordResetRequest.getEmail()).orElseThrow(() -> {
            logger.error("User not found with email: {}", passwordResetRequest.getEmail());
            return new UserNotFoundException("User not found with email: " + passwordResetRequest.getEmail());
        });
        if (userInDb.isDeleted() || !userInDb.isActive()) {
            logger.debug("User is not active or deleted: {}", passwordResetRequest.getEmail());
            throw new AccessDeniedException();
        }
        userInDb.setResetPasswordToken(UUID.randomUUID().toString());
        userRepository.save(userInDb);
        emailHelper.sendTokenEmail(passwordResetRequest.getEmail(), userInDb.getResetPasswordToken());
    }
    @Override
    public void activateUser(UserActivateRequest userActivateRequest) {

        User userInDb = userRepository.findByEmail(userActivateRequest.getEmail()).orElseThrow(() -> {
            logger.error("User not found with email: {}", userActivateRequest.getEmail());
            return new UserNotFoundException("User not found with email: " + userActivateRequest.getEmail());
        });

        if (!userInDb.isDeleted()) {
            logger.debug("User is already active: {}", userActivateRequest.getEmail());
            throw new RuntimeException("User is already active: " + userActivateRequest.getEmail());
        }

        userInDb.setDeleted(false);
        userRepository.save(userInDb);
        logger.info("User activated: {}", userActivateRequest.getEmail());
    }

    @Override
    public void userUnblock(UserUnblockRequest userUnblockRequest) {

        User userInDb = userRepository.findByEmail(userUnblockRequest.getEmail()).orElseThrow(() -> {
            logger.error("User not found with email: {}", userUnblockRequest.getEmail());
            return new UserNotFoundException("User not found with email: " + userUnblockRequest.getEmail());
        });
        if (userInDb.getLoginAttempts() < 5) {
            logger.debug("User is not blocked: {}", userUnblockRequest.getEmail());
            throw new RuntimeException("User is not blocked: " + userUnblockRequest.getEmail());
        }
        userInDb.setLoginAttempts(0);
        userRepository.save(userInDb);
        logger.info("User unblocked: {}", userUnblockRequest.getEmail());
    }

    @Override
    public void updatePassword(String token, UpdatePasswordRequest request) {
        User userInDb = userRepository.findByResetPasswordToken(token).orElseThrow(() -> {
            logger.error("Invalid password reset token: {}", token);
            return new RuntimeException("Invalid password reset token: " + token);
        });
        userInDb.setPassword(passwordEncoder.encode(request.getPassword()));
        userInDb.setResetPasswordToken(null);
        userRepository.save(userInDb);
        logger.debug("Password updated successfully: {}", userInDb.getEmail());
    }
}
