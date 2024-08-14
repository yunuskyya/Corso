package com.infina.corso.service.impl;

import com.infina.corso.dto.request.*;
import com.infina.corso.dto.response.GetAllUserResponse;
import com.infina.corso.exception.*;
import com.infina.corso.model.Transaction;
import com.infina.corso.model.User;
import com.infina.corso.model.enums.Role;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.UserService;
import com.infina.corso.util.EmailHelper;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final EmailHelper emailHelper;
    private final UserRepository userRepository;
    private final ModelMapper modelMapperForResponse;
    private final ModelMapper modelMapperForRequest;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, @Qualifier("modelMapperForResponse") ModelMapper modelMapperForResponse,
                           @Qualifier("modelMapperForRequest") ModelMapper modelMapperForRequest, EmailHelper emailHelper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapperForResponse = modelMapperForResponse;
        this.modelMapperForRequest = modelMapperForRequest;
        this.emailHelper = emailHelper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<GetAllUserResponse> getAllUser(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> modelMapperForResponse.map(user, GetAllUserResponse.class));
    }

    @Override
    public void registerBroker(@Valid RegisterUserRequest registerUserRequest) {
        User newUser = modelMapperForRequest.map(registerUserRequest, User.class);
        newUser.setPassword(passwordEncoder.encode(""));
        newUser.setAuthorities(new HashSet<>() {{
            add(Role.ROLE_BROKER);
        }});
        newUser.setActive(true);
        newUser.setResetPasswordToken(UUID.randomUUID().toString());
        userRepository.save(newUser);
        logger.info("Broker registered: {}", newUser.getUsername());
        emailHelper.sendTokenEmail(newUser.getEmail(), newUser.getResetPasswordToken());
    }

    @Override
    public void registerManager(@Valid RegisterManagerRequest request) {
        User newUser = modelMapperForRequest.map(request, User.class);
        String rawPassword = request.getPassword();
        newUser.setPassword(passwordEncoder.encode(rawPassword));
        newUser.setAuthorities(new HashSet<>() {{
            add(Role.ROLE_MANAGER);
        }});
        newUser.setActive(true);
        userRepository.save(newUser);
        logger.info("Manager registered: {}", newUser.getEmail());
        emailHelper.sendRegistrationEmail(request);

    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            logger.info("eski şifre yanlış: {}", user.getEmail());
            throw new PasswordMismatchException("Old password is incorrect");
        }

        String encodedNewPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        user.setPassword(encodedNewPassword);
        emailHelper.sendPasswordChangeNotification(user);
        userRepository.save(user);
    }

    public List<Transaction> getAllTransactionsById(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.get().getTransactions();
    }

    @Override
    public void deleteUser(int id) {
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
            throw new GeneralErrorException("user.already.active.error.message");
        }

        userInDb.setDeleted(false);
        userRepository.save(userInDb);
        logger.info("User activated: {}", userActivateRequest.getEmail());
    }

    @Override
    public Page<GetAllUserResponse> getAllBrokers(Pageable pageable) {
        Page<User> brokersPage = userRepository.findAllByAuthorities(Role.ROLE_BROKER, pageable);

        if (brokersPage.isEmpty()) {
            logger.error("No broker found");
            throw new UserNotFoundException("No broker found");
        }

        return brokersPage.map(user -> modelMapperForResponse.map(user, GetAllUserResponse.class));
    }


    @Override
    public void userUnblock(UserUnblockRequest userUnblockRequest) {

        User userInDb = userRepository.findByEmail(userUnblockRequest.getEmail()).orElseThrow(() -> {
            logger.error("User not found with email: {}", userUnblockRequest.getEmail());
            return new UserNotFoundException("User not found with email: " + userUnblockRequest.getEmail());
        });
        if (!userInDb.isAccountLocked()){
            logger.debug("User is not blocked: {}", userUnblockRequest.getEmail());
            throw new UserNotBlockedException();
        }
        userInDb.setAccountLocked(false);
        userRepository.save(userInDb);
        logger.info("User unblocked: {}", userUnblockRequest.getEmail());
    }

    @Override
    public void updatePassword(String token, UpdatePasswordRequest request) {
        User userInDb = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid password reset token."));

        userInDb.setPassword(passwordEncoder.encode(request.getPassword()));
        userInDb.setResetPasswordToken(null);
        userRepository.save(userInDb);
        logger.debug("Password updated successfully: {}", userInDb.getEmail());
    }
}
