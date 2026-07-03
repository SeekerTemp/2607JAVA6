package com.vn.test.demob1.LAB.security;

import com.vn.test.demob1.LAB.dao.UserDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * LAB 2 - Cấu hình hoạt động (active):
 *  - Bài 1: Authorization phân quyền theo vai trò cho /poly/url1..url4
 *  - Bài 3: Nguồn dữ liệu người dùng tùy biến DaoUserDetailsManager (CSDL J6Security2)
 *
 * (Bài 2 - JdbcUserDetailsManager được để lại dạng tham khảo ở cuối lớp)
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // Bài 3: Nguồn dữ liệu người dùng tùy biến (DAO) - đang được sử dụng
    @Bean
    public UserDetailsService userDetailsService(UserDAO userDAO) {
        return new DaoUserDetailsManager(userDAO);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Bỏ CSRF và CORS
        http.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable());

        // Bài 1: Phân quyền truy xuất theo vai trò
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/poly/url1").authenticated()
                .requestMatchers("/poly/url2").hasRole("USER")
                .requestMatchers("/poly/url3").hasRole("ADMIN")
                .requestMatchers("/poly/url4").hasAnyRole("USER", "ADMIN")
                .anyRequest().permitAll()
        );

        // Form đăng nhập tùy biến
        http.formLogin(form -> form
                .loginPage("/login/form")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/login/success", true)
                .failureUrl("/login/failure")
                .permitAll()
        );

        // Ghi nhớ tài khoản
        http.rememberMe(remember -> remember.tokenValiditySeconds(3 * 24 * 60 * 60));

        // Đăng xuất
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login/exit")
        );

        // Xử lý từ chối truy xuất (sai vai trò) -> access-denied.html
        http.exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"));

        return http.build();
    }

    /*
     * ------- THAM KHẢO: Bài 2 - JdbcUserDetailsManager (CSDL J6Security) -------
     * Đổi datasource sang J6Security và thay bean userDetailsService phía trên bằng:
     *
     * @Bean
     * public UserDetailsService userDetailsService(DataSource dataSource) {
     *     return new JdbcUserDetailsManager(dataSource);
     * }
     *
     * ------- THAM KHẢO: Bài 1 - InMemoryUserDetailsManager -------
     * @Bean
     * public UserDetailsService userDetailsService(PasswordEncoder pe) {
     *     UserDetails user1 = User.withUsername("user@gmail.com").password(pe.encode("123")).roles("USER").build();
     *     UserDetails user2 = User.withUsername("admin@gmail.com").password(pe.encode("123")).roles("ADMIN").build();
     *     UserDetails user3 = User.withUsername("both@gmail.com").password(pe.encode("123")).roles("USER", "ADMIN").build();
     *     return new InMemoryUserDetailsManager(user1, user2, user3);
     * }
     */
}
