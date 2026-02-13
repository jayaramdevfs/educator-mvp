package com.educator.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthRateLimitFilter authRateLimitFilter;
    private final List<String> corsAllowedOrigins;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthRateLimitFilter authRateLimitFilter,
            @Value("${app.security.cors.allowed-origins:http://localhost:3000,http://127.0.0.1:3000}")
            List<String> corsAllowedOrigins
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authRateLimitFilter = authRateLimitFilter;
        this.corsAllowedOrigins = corsAllowedOrigins;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF is disabled for stateless JWT API requests.
                .csrf(csrf -> csrf.disable())

                // CORS uses the explicit configuration source declared below.
                .cors(Customizer.withDefaults())

                // JWT-only stateless sessions.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Return explicit status codes for authn/authz failures.
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                writeJsonError(
                                        response,
                                        request.getRequestURI(),
                                        HttpServletResponse.SC_UNAUTHORIZED,
                                        "UNAUTHORIZED",
                                        "Authentication required"
                                ))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                writeJsonError(
                                        response,
                                        request.getRequestURI(),
                                        HttpServletResponse.SC_FORBIDDEN,
                                        "FORBIDDEN",
                                        "Access denied"
                                ))
                )

                // Harden HTTP response headers for browser clients.
                .headers(headers -> headers
                        .frameOptions(frame -> frame.deny())
                        .contentTypeOptions(Customizer.withDefaults())
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)
                        )
                )

                // Authorization rules.
                .authorizeHttpRequests(auth -> auth

                        // CORS preflight.
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Public endpoints.
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/public/**"
                        ).permitAll()

                        // Health checks for orchestration and uptime probes.
                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/health/**"
                        ).permitAll()

                        // Public hierarchy read-only endpoints.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/hierarchy/roots",
                                "/api/hierarchy/*/children"
                        ).permitAll()

                        // Admin-only endpoints.
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // Authenticated endpoints.
                        .requestMatchers(
                                "/api/learner/**",
                                "/api/student/**",
                                "/api/instructor/**",
                                "/api/hierarchy/**"
                        ).authenticated()

                        // Everything else requires login.
                        .anyRequest()
                        .authenticated()
                )

                // JWT authentication filter.
                .addFilterBefore(
                        authRateLimitFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterAfter(
                        jwtAuthenticationFilter,
                        AuthRateLimitFilter.class
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsAllowedOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    private void writeJsonError(
            HttpServletResponse response,
            String path,
            int status,
            String code,
            String message
    ) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String errorName = status == 401 ? "Unauthorized" : "Forbidden";
        String json = String.format(
                "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"code\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}",
                escape(OffsetDateTime.now().toString()),
                status,
                errorName,
                escape(code),
                escape(message),
                escape(path)
        );

        response.getWriter().write(json);
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
