package com.vn.test.demob1.LAB.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * LAB 5 - Phân quyền cho REST API dùng JWT.
 * <p>
 * Tài khoản khai báo InMemory theo Lab 6:
 * <pre>
 * user@gmail.com  / 123 -> USER
 * admin@gmail.com / 123 -> ADMIN
 * both@gmail.com  / 123 -> USER, ADMIN
 * </pre>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder pe) {
        UserDetails user1 = User.withUsername("user@gmail.com")
                .password(pe.encode("123")).roles("USER").build();
        UserDetails user2 = User.withUsername("admin@gmail.com")
                .password(pe.encode("123")).roles("ADMIN").build();
        UserDetails user3 = User.withUsername("both@gmail.com")
                .password(pe.encode("123")).roles("USER", "ADMIN").build();
        return new InMemoryUserDetailsManager(user1, user2, user3);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter)
            throws Exception {

        // REST API cho Postman: không dùng session, không cần CSRF
        http.csrf(config -> config.disable()).cors(config -> config.disable());

        http.authorizeHttpRequests(config -> {
            config.requestMatchers("/user").hasRole("USER");     // chỉ vai người dùng
            config.requestMatchers("/admin").hasRole("ADMIN");   // chỉ vai quản trị viên
            config.requestMatchers("/logout").authenticated();   // phải có JWT hợp lệ
            config.anyRequest().permitAll();
        });

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Tắt logout mặc định của Spring Security để POST /logout đi vào controller
        http.logout(logout -> logout.disable());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
