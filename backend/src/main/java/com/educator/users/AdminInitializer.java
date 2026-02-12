package com.educator.users;

import com.educator.roles.Role;
import com.educator.roles.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String bootstrapAdminEmail;
    private final String bootstrapAdminPassword;

    public AdminInitializer(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.bootstrap.email:}") String bootstrapAdminEmail,
            @Value("${app.admin.bootstrap.password:}") String bootstrapAdminPassword
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.bootstrapAdminEmail = bootstrapAdminEmail == null ? "" : bootstrapAdminEmail.trim();
        this.bootstrapAdminPassword = bootstrapAdminPassword == null ? "" : bootstrapAdminPassword;
    }

    @Override
    public void run(String... args) {
        if (bootstrapAdminEmail.isBlank() || bootstrapAdminPassword.isBlank()) {
            return;
        }

        Role adminRole = roleRepository.findByName(Role.ADMIN)
                .orElseThrow(() -> new IllegalStateException("ADMIN role not found"));

        userRepository.findByEmail(bootstrapAdminEmail).ifPresentOrElse(
                existingUser -> {
                    if (existingUser.hasRole(Role.ADMIN)) {
                        return;
                    }
                    existingUser.addRole(adminRole);
                    userRepository.save(existingUser);
                    log.info("Granted ADMIN role to bootstrap user: {}", bootstrapAdminEmail);
                },
                () -> {
                    User adminUser = new User(
                            bootstrapAdminEmail,
                            passwordEncoder.encode(bootstrapAdminPassword)
                    );
                    adminUser.addRole(adminRole);
                    userRepository.save(adminUser);
                    log.info("Created bootstrap ADMIN user: {}", bootstrapAdminEmail);
                }
        );
    }
}
