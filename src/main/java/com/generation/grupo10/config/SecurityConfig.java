package com.generation.grupo10.config;

import com.generation.grupo10.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth

                        // Cualquiera puede entrar
                        .requestMatchers(
                                "/auth/login",
                                "/auth/registro",
                                "/auth/verificar-correo",
                                "/auth/forgot-password",
                                "/auth/reset-password",
                                "/auth/validar-cambio-password",
                                "/auth/validar-edicion-perfil",

                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",

                                "/api/servicios/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/canchas/**"
                        ).permitAll()

                        // Gestión de canchas solamente para ADMIN
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/canchas/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/canchas/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/canchas/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/api/reservas/admin/**"
                        ).hasRole("ADMIN")

                        // Solo ADMIN
                        .requestMatchers(
                                "/api/usuarios/**"
                        ).hasRole("ADMIN")

                        // Cualquier usuario autenticado
                        .requestMatchers(
                                "/api/reservas/**"
                        ).authenticated()

                        // Perfil
                        .requestMatchers(
                                "/api/perfil/**"
                        ).authenticated()

                        // Lo demás
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        // Frontend de producción
        configuration.setAllowedOrigins(
                List.of(
                        "https://cancheros-proyecto.vercel.app"
                )
        );

        // Métodos permitidos
        configuration.setAllowedMethods(
                Arrays.asList(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        // Headers permitidos
        configuration.setAllowedHeaders(
                List.of("*")
        );

        // Permite enviar/recibir cookies
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}