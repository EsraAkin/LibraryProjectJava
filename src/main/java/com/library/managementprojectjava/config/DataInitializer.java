package com.library.managementprojectjava.config;

import com.library.managementprojectjava.entity.user.Role;
import com.library.managementprojectjava.entity.user.User;
import com.library.managementprojectjava.repository.user.RoleRepository;
import com.library.managementprojectjava.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            Role adminRole = Role.builder().name("ADMIN").build();
            Role staffRole = Role.builder().name("STAFF").build();
            Role memberRole = Role.builder().name("MEMBER").build();

            roleRepository.saveAll(List.of(adminRole, staffRole, memberRole));
            System.out.println("Roller eklendi");
        }

        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();

            User user = User.builder()
                    .firstName("Esra")
                    .lastName("Demo")
                    .address("Demo Mah. No:1")
                    .phone("555-555-5555")
                    .birthDate(LocalDate.of(1995, 1, 1))
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("123456"))
                    .roles(Set.of(adminRole))
                    .builtIn(true)
                    .build();

            userRepository.save(user);
            System.out.println("Admin kullanıcısı eklendi");
        }
    }
}
