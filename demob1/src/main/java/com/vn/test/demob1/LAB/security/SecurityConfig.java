package com.vn.test.demob1.LAB.security;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

/**
 * LAB 3 - Cấu hình hoạt động (active):
 *  - Bài 1: Đăng nhập từ Google (OAuth2) + tạo Authentication mới sau khi login
 *  - Bài 2: Phân quyền qua phương thức với @PreAuthorize (xem MyController)
 *
 * Nguồn dữ liệu: InMemoryUserDetailsManager (kế thừa Lab2.1).
 * SecurityFilterChain cho phép truy cập tất cả; việc phân quyền được thực
 * hiện tại tầng phương thức bằng @PreAuthorize.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Bỏ CSRF và CORS
        http.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable());

        // Bài 2: cho phép truy cập tất cả, phân quyền bằng @PreAuthorize ở controller
        http.authorizeHttpRequests(req -> req.anyRequest().permitAll());

        // Form đăng nhập tùy biến
        http.formLogin(form -> form
                .loginPage("/login/form")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/login/success", true)
                .failureUrl("/login/failure")
                .permitAll()
        );

        // Bài 1: Đăng nhập từ mạng xã hội (Google)
        http.oauth2Login(login -> {
            login.loginPage("/login/form");
            login.permitAll();
            // Tạo đối tượng Authentication mới thay thế Authentication từ Google
            login.successHandler((request, response, authentication) -> {
                DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
                String username = user.getEmail();
                String role = "OAUTH";

                UserDetails newUser = User.withUsername(username).password("{noop}").roles(role).build();
                Authentication newauth = new UsernamePasswordAuthenticationToken(
                        newUser, null, newUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(newauth);

                // Quay về trang được bảo vệ trước đó hoặc trang chủ
                HttpSession session = request.getSession();
                String attr = "SPRING_SECURITY_SAVED_REQUEST";
                DefaultSavedRequest saved = (DefaultSavedRequest) session.getAttribute(attr);
                String redirectUrl = (saved == null) ? "/" : saved.getRedirectUrl();
                response.sendRedirect(redirectUrl);
            });
        });

        // Đăng xuất
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login/exit")
        );

        // Từ chối truy xuất (thiếu quyền) -> access-denied
        http.exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"));

        return http.build();
    }
}
