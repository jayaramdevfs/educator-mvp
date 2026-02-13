package com.educator.admin;

import com.educator.roles.Role;
import com.educator.roles.RoleRepository;
import com.educator.users.User;
import com.educator.users.UserRepository;
import com.educator.users.dto.AdminUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AdminUserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public Page<AdminUserResponse> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toResponse);
    }

    public AdminUserResponse updateRoles(Long userId, List<String> roleNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            String normalizedRole = roleName.trim().toUpperCase(Locale.ROOT);
            Role role = roleRepository.findByName(normalizedRole)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleName));
            roles.add(role);
        }

        user.setRoles(roles);
        User updatedUser = userRepository.save(user);
        return toResponse(updatedUser);
    }

    private AdminUserResponse toResponse(User user) {
        return new AdminUserResponse(
                user.getId(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
    }
}
