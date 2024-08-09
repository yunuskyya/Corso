package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.ChangePasswordRequest;
import com.infina.corso.dto.request.RegisterUserRequest;
import com.infina.corso.dto.response.GetAllUserResponse;
import com.infina.corso.exception.UserNotFoundException;
import com.infina.corso.model.Transaction;
import com.infina.corso.model.User;
import com.infina.corso.model.enums.Role;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.MailService;
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

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final EmailHelper emailHelper;
    private UserRepository userRepository;
    private ModelMapperConfig mapper;
    private PasswordEncoder passwordEncoder;
    private MailService emailService;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Override
    public Page<GetAllUserResponse> getAllUser(Pageable pageable) {
        Page<User> brokers = userRepository.findByAuthoritiesContaining(Role.ROLE_BROKER, pageable);
        return brokers.map(user -> mapper.modelMapperForResponse().map(user, GetAllUserResponse.class));
    }
    @Override
    public void registerBroker(@Valid RegisterUserRequest registerUserRequest) {
        User newUser = mapper.modelMapperForRequest().map(registerUserRequest, User.class);
        String rawPassword = registerUserRequest.getPassword();  // Ham şifreyi saklayın
        newUser.setPassword(passwordEncoder.encode(rawPassword));
        newUser.setAuthorities(new HashSet<>() {{
            add(Role.ROLE_BROKER);
        }});
        newUser.setActive(true);
        userRepository.save(newUser);
        logger.info("Broker registered: {}", newUser.getUsername());
        emailHelper.sendRegistrationEmail(newUser, rawPassword);
    }
    @Override
    public void registerManager(@Valid RegisterUserRequest registerUserRequest) {
        User newUser = mapper.modelMapperForRequest().map(registerUserRequest, User.class);
        String rawPassword = registerUserRequest.getPassword();  // Ham şifreyi saklayın
        newUser.setPassword(passwordEncoder.encode(rawPassword));
        newUser.setAuthorities(new HashSet<>() {{
            add(Role.ROLE_MANAGER);
        }});
        newUser.setActive(true);
        userRepository.save(newUser);
        logger.info("Manager registered: {}", newUser.getEmail());
        emailHelper.sendRegistrationEmail(newUser, rawPassword);

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

    @Override
    public void activateUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (!user.isAccountLocked()) {
            throw new RuntimeException("Hesap zaten aktif.");
        }

        user.setAccountLocked(false);
        userRepository.save(user);
        logger.info("Kullanıcı aktifleştirildi: {}", user.getUsername());
        emailHelper.sendActivationEmail(user);
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
}
