package com.infina.corso.bootstrap;

import com.infina.corso.model.User;
import com.infina.corso.model.enums.Role;
import com.infina.corso.repository.CurrencyRepository;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.CurrencyService;
import com.infina.corso.service.impl.CurrencyServiceImp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrencyRepository currencyRepository;
    private final CurrencyService currencyService;


    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder, CurrencyRepository currencyRepository, CurrencyService currencyService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.currencyRepository = currencyRepository;
        this.currencyService = currencyService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            //userRepository.deleteAll();
            addUsers();
        }
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

        // manager:
        User manager = User.builder()
                .username("manager")
                .email("manager@corso.com")
                .password(passwordEncoder.encode("manager"))
                .authorities(Set.of(Role.ROLE_MANAGER))
                .isDeleted(false)
                .active(true)
                .build();

        // broker:
        User broker = User.builder()
                .username("broker")
                .email("broker@corso.com")
                .password(passwordEncoder.encode("broker"))
                .authorities(Set.of(Role.ROLE_BROKER))
                .isDeleted(false)
                .active(true)
                .build();

        userRepository.saveAll(Arrays.asList(admin, manager, broker));
    }
}
