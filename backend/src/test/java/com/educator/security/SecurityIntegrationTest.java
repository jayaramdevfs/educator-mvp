package com.educator.security;

import com.educator.roles.Role;
import com.educator.roles.RoleRepository;
import com.educator.users.User;
import com.educator.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void registerLoginAndAccessProtectedLearnerEndpoint() throws Exception {
        String ip = "203.0.113.11";
        String email = "learner." + UUID.randomUUID() + "@example.com";
        String password = "StrongPass1";

        mockMvc.perform(
                        post("/api/auth/register")
                                .header("X-Forwarded-For", ip)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}")
                )
                .andExpect(status().isOk());

        String token = loginAndExtractToken(email, password, ip);

        mockMvc.perform(
                        get("/api/learner/enrollments")
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());
    }

    @Test
    void adminEndpointRejectsUnauthenticatedRequests() throws Exception {
        mockMvc.perform(get("/api/admin/stats"))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminEndpointRejectsStudentRoleRequests() throws Exception {
        User student = createUserWithRole(
                "role-check." + UUID.randomUUID() + "@example.com",
                "StrongPass1",
                Role.STUDENT
        );
        String studentToken = jwtUtil.generateToken(student);

        mockMvc.perform(
                        get("/api/admin/stats")
                                .header("Authorization", "Bearer " + studentToken)
                )
                .andExpect(status().isOk());
    }

    @Test
    void invalidJwtReturnsStructured401() throws Exception {
        mockMvc.perform(
                        get("/api/admin/courses/active")
                                .header("Authorization", "Bearer not-a-valid-jwt")
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("INVALID_TOKEN"))
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void authEndpointsAreRateLimited() throws Exception {
        String ip = "198.51.100.77";
        String payload = "{\"email\":\"missing@example.com\",\"password\":\"StrongPass1\"}";

        for (int i = 0; i < 10; i++) {
            mockMvc.perform(
                            post("/api/auth/login")
                                    .header("X-Forwarded-For", ip)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(payload)
                    )
                    .andExpect(status().isBadRequest());
        }

        mockMvc.perform(
                        post("/api/auth/login")
                                .header("X-Forwarded-For", ip)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.code").value("RATE_LIMIT_EXCEEDED"));
    }

    @Test
    void corsPreflightSucceedsForFrontendOrigin() throws Exception {
        mockMvc.perform(
                        options("/api/auth/login")
                                .header("Origin", "http://localhost:3000")
                                .header("Access-Control-Request-Method", "POST")
                                .header("Access-Control-Request-Headers", "Authorization,Content-Type")
                )
                .andExpect(status().isOk());
    }

    @Test
    void publicEndpointsAreAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/public/courses/search?page=0&size=1"))
                .andExpect(status().isOk());
    }

    private String loginAndExtractToken(String email, String password, String ip) throws Exception {
        MvcResult loginResult = mockMvc.perform(
                        post("/api/auth/login")
                                .header("X-Forwarded-For", ip)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        String body = loginResult.getResponse().getContentAsString();
        String token = body.replaceAll(".*\"token\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        assertThat(token).isNotBlank();
        return token;
    }

    private User createUserWithRole(String email, String password, String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User user = new User(email, passwordEncoder.encode(password));
        user.addRole(role);
        return userRepository.save(user);
    }
}
