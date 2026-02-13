package com.educator.repository;

import com.educator.roles.Role;
import com.educator.roles.RoleRepository;
import com.educator.users.User;
import com.educator.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryDataJpaTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void countByRoleName_countsDistinctUsersByRole() {
        Role student = roleRepository.save(new Role(Role.STUDENT));
        Role admin = roleRepository.save(new Role(Role.ADMIN));

        User user1 = new User("student1@example.com", "pw");
        user1.addRole(student);
        userRepository.save(user1);

        User user2 = new User("student2@example.com", "pw");
        user2.addRole(student);
        userRepository.save(user2);

        User user3 = new User("admin@example.com", "pw");
        user3.addRole(admin);
        userRepository.save(user3);

        assertThat(userRepository.countByRoleName(Role.STUDENT)).isEqualTo(2L);
        assertThat(userRepository.countByRoleName(Role.ADMIN)).isEqualTo(1L);
        assertThat(userRepository.countByRoleName(Role.INSTRUCTOR)).isZero();
    }
}


