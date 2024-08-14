package com.infina.corso.bootstrap;

import com.infina.corso.model.User;
import com.infina.corso.model.enums.Role;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.CurrencyService;
import com.infina.corso.service.SystemDateService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SystemDateService systemDateService;
    private final CurrencyService currencyService;


    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder, SystemDateService systemDateService, CurrencyService currencyService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.systemDateService = systemDateService;
        this.currencyService = currencyService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            //userRepository.deleteAll();
            addUsers();
        }
        systemDateService.initializeSystemDate();
    }

    private void addUsers() {
        // admin:
        User admin = User.builder()
                .username("admin")
                .email("admin@corso.com")
                .password(passwordEncoder.encode("admin"))
                .authorities(Set.of(Role.ROLE_ADMIN))
                .isDeleted(false)
                .active(true)
                .build();
        userRepository.saveAll(Arrays.asList(admin));
    }
}
