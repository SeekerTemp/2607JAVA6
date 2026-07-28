package com.vn.test.demob1.LAB.security;

import jakarta.servlet.http.HttpServletResponse;
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

import java.io.IOException;

/**
 * LAB 6 - Bài 3: Phân quyền REST API và xác thực bằng JWT (STATELESS).
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
        UserDetails user1 = User.withUsername("user@gmail.com").password(pe.encode("123")).roles("USER").build();
        UserDetails user2 = User.withUsername("admin@gmail.com").password(pe.encode("123")).roles("ADMIN").build();
        UserDetails user3 = User.withUsername("both@gmail.com").password(pe.encode("123")).roles("USER", "ADMIN").build();
        return new InMemoryUserDetailsManager(user1, user2, user3);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http.csrf(config -> config.disable()).cors(config -> config.disable());

        http.authorizeHttpRequests(config -> {
            // LAB 6 - Bài 3
            config.requestMatchers("/poly/url1").authenticated();
            config.requestMatchers("/poly/url2").hasRole("USER");
            config.requestMatchers("/poly/url3").hasRole("ADMIN");
            config.requestMatchers("/poly/url4").hasAnyRole("USER", "ADMIN");

            // ASM LAB 6
            config.requestMatchers("/jwt-generator", "/jwt-decoder/**", "/login").permitAll();
            config.requestMatchers("/user").hasRole("USER");
            config.requestMatchers("/admin").hasRole("ADMIN");
            config.requestMatchers("/logout").authenticated();

            config.anyRequest().permitAll();
        });

        // Vô hiệu hóa LogoutFilter mặc định của Spring Security (nó chiếm POST /logout)
        // để AsmJwtApi.logout() tự xử lý việc vô hiệu hóa JWT.
        http.logout(logout -> logout.disable());

        // Chưa đăng nhập -> 401, sai vai trò -> 403 (kèm JSON thay vì trang HTML)
        http.exceptionHandling(handling -> {
            handling.authenticationEntryPoint((request, response, ex) -> writeError(
                    response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Yêu cầu JWT hợp lệ ở header: Authorization: Bearer <jwt>"));
            handling.accessDeniedHandler((request, response, ex) -> writeError(
                    response, HttpServletResponse.SC_FORBIDDEN,
                    "Tài khoản không có quyền truy xuất tài nguyên này"));
        });

        // Không duy trì user trong session (STATELESS)
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Nạp bộ lọc JWT trước bộ lọc xác thực username/password
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /** Trả lỗi dạng JSON cho REST client. */
    private static void writeError(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}
