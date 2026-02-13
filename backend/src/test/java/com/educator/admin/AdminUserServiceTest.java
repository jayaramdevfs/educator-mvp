package com.educator.admin;

import com.educator.roles.Role;
import com.educator.roles.RoleRepository;
import com.educator.users.User;
import com.educator.users.UserRepository;
import com.educator.users.dto.AdminUserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AdminUserService service;

    @Test
    void getUsers_mapsPagedResults() {
        User user = user(10L, "admin@example.com", Set.of(new Role(Role.ADMIN)));
        Pageable pageable = PageRequest.of(0, 20);
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(user)));

        Page<AdminUserResponse> result = service.getUsers(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(10L);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("admin@example.com");
        assertThat(result.getContent().get(0).getRoles()).containsExactly("ADMIN");
    }

    @Test
    void updateRoles_updatesRolesWithNormalizedInput() {
        User user = user(1L, "user@example.com", Set.of(new Role(Role.STUDENT)));
        Role adminRole = new Role(Role.ADMIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(adminRole));
        when(userRepository.save(user)).thenReturn(user);

        AdminUserResponse response = service.updateRoles(1L, List.of("  admin  "));

        assertThat(response.getRoles()).containsExactly("ADMIN");
        assertThat(user.getRoles()).containsExactly(adminRole);
    }

    @Test
    void updateRoles_deduplicatesSameRoleWhenRepositoryReturnsSameRoleEntity() {
        User user = user(1L, "user@example.com", Set.of(new Role(Role.STUDENT)));
        Role studentRole = new Role(Role.STUDENT);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("STUDENT")).thenReturn(Optional.of(studentRole));
        when(userRepository.save(user)).thenReturn(user);

        AdminUserResponse response = service.updateRoles(1L, List.of("STUDENT", "student"));

        assertThat(response.getRoles()).containsExactly("STUDENT");
        assertThat(user.getRoles()).hasSize(1);
    }

    @Test
    void updateRoles_allowsClearingRolesWhenEmptyListProvided() {
        User user = user(1L, "user@example.com", Set.of(new Role(Role.STUDENT)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        AdminUserResponse response = service.updateRoles(1L, List.of());

        assertThat(response.getRoles()).isEmpty();
        assertThat(user.getRoles()).isEmpty();
    }

    @Test
    void updateRoles_throwsWhenUserMissing() {
        when(userRepository.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateRoles(9L, List.of("ADMIN")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found");
    }

    @Test
    void updateRoles_throwsWhenRoleInvalid() {
        User user = user(1L, "user@example.com", Set.of(new Role(Role.STUDENT)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("INVALID")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateRoles(1L, List.of("invalid")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid role: invalid");
    }

    @Test
    void updateRoles_doesNotSaveWhenRoleInvalid() {
        User user = user(1L, "user@example.com", Set.of(new Role(Role.STUDENT)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("INVALID")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateRoles(1L, List.of("invalid")))
                .isInstanceOf(IllegalArgumentException.class);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateRoles_looksUpRolesUsingTrimmedUppercaseNames() {
        User user = user(1L, "user@example.com", Set.of(new Role(Role.STUDENT)));
        Role instructorRole = new Role(Role.INSTRUCTOR);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("INSTRUCTOR")).thenReturn(Optional.of(instructorRole));
        when(userRepository.save(user)).thenReturn(user);

        service.updateRoles(1L, List.of("  instructor "));

        verify(roleRepository).findByName("INSTRUCTOR");
    }

    @Test
    void updateRoles_returnsResponseWithUpdatedIdentity() {
        User user = user(55L, "instructor@example.com", Set.of(new Role(Role.INSTRUCTOR)));
        Role adminRole = new Role(Role.ADMIN);
        when(userRepository.findById(55L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(adminRole));
        when(userRepository.save(user)).thenReturn(user);

        AdminUserResponse response = service.updateRoles(55L, List.of("ADMIN"));

        assertThat(response.getId()).isEqualTo(55L);
        assertThat(response.getEmail()).isEqualTo("instructor@example.com");
        assertThat(response.getRoles()).containsExactly("ADMIN");
    }

    private static User user(Long id, String email, Set<Role> roles) {
        User user = new User(email, "encoded-password");
        user.setRoles(roles);
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}

