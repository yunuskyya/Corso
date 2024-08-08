package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.ChangePasswordRequest;
import com.infina.corso.dto.request.RegisterUserRequest;
import com.infina.corso.dto.response.GetAllUserResponse;
import com.infina.corso.model.Transaction;
import com.infina.corso.model.User;
import com.infina.corso.model.enums.Role;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.MailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements com.infina.corso.service.UserService {

    private UserRepository userRepository;
    private ModelMapperConfig mapper;
    private PasswordEncoder passwordEncoder;
    private MailService emailService;
    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Override
    public List<GetAllUserResponse> getAllUser() {
        List<User> brokers = userRepository.findByAuthoritiesContaining(Role.ROLE_BROKER);
        return brokers.stream()
                .map(user -> mapper.modelMapperForResponse().map(user, GetAllUserResponse.class))
                .collect(Collectors.toList());
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
        String subject = "Kayıt Onayı";
        String text = String.format(
                "Merhaba %s %s,\n\n" +
                        "Sistemimize başarıyla kayıt oldunuz.\n\n" +
                        "Kullanıcı Adınız: %s\n" +
                        "Geçici Şifreniz: %s\n\n" +
                        "Güvenliğiniz için lütfen şifrenizi değiştirmek için sisteme giriş yapın veya aşağıdaki bağlantıyı kullanın:\n" +
                        "http://localhost:8080/change-password\n\n" +
                        "Teşekkürler,\n" +
                        "Infina Corso Ekibi",
                newUser.getFirstName(),
                newUser.getLastName(),
                newUser.getUsername(),
                rawPassword
        );
        emailService.sendSimpleMessage(newUser.getEmail(), subject, text);
    }
    @Override
    public void registerManager(@Valid RegisterUserRequest registerUserRequest) {
        User newUser = mapper.modelMapperForRequest().map(registerUserRequest, User.class);
        newUser.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        newUser.setAuthorities(new HashSet<>() {{
            add(Role.ROLE_MANAGER);
        }});
        newUser.setActive(true);
        userRepository.save(newUser);
        logger.info("Manager registered: {}", newUser.getEmail());
        String subject = "Kayıt Onayı";
        String text = String.format(
                "Merhaba %s %s,\n\n" +
                        "Sistemimize başarıyla kayıt oldunuz.\n\n" +
                        "Kullanıcı Adınız: %s\n" +
                        "Geçici Şifreniz: %s\n\n" +
                        "Güvenliğiniz için lütfen şifrenizi değiştirmek için sisteme giriş yapın veya aşağıdaki bağlantıyı kullanın:\n" +
                        "http://localhost:8080/change-password\n\n" +
                        "Teşekkürler,\n" +
                        "Infina Corso Ekibi",
                newUser.getFirstName(),
                newUser.getLastName(),
                newUser.getUsername(),
                registerUserRequest.getPassword()
        );
        emailService.sendSimpleMessage(newUser.getEmail(), subject, text);
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

        String subject = "Hesap Aktifleştirildi";
        String text = String.format(
                "Merhaba %s %s,\n\n" +
                        "Hesabınız başarıyla aktifleştirildi.\n\n" +
                        "Teşekkürler,\n" +
                        "Infina Corso Ekibi",
                user.getFirstName(),
                user.getLastName()
        );
        emailService.sendSimpleMessage(user.getEmail(), subject, text);
    }


    public List<Transaction> getAllTransactionsById(int id){
        Optional<User> user = userRepository.findById(id);
        return user.get().getTransactions();
    }
}
