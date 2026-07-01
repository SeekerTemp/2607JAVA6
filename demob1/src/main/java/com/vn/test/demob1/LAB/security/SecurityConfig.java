package com.vn.test.demob1.LAB.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
//Config lab 2
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        String password = passwordEncoder.encode("123");
        System.out.println(password);
        UserDetails user1 = User.withUsername("user@gmail.com").password(password).roles("USER").build();
        UserDetails user2 = User.withUsername("admin@gmail.com").password(password).roles("ADMIN").build();
        UserDetails user3 = User.withUsername("both@gmail.com").password(password).roles("USER", "ADMIN").build();

        return new InMemoryUserDetailsManager(user1, user2, user3);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {http.csrf(csrf
                    -> csrf.disable()).cors(cors
                    -> cors.disable()).authorizeHttpRequests(auth
                    -> auth.requestMatchers("/poly/**").authenticated()
                    .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login/form")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/login/success", true)
                        .failureUrl("/login/failure")
                        .permitAll()
                )
                .rememberMe(remember ->
                        remember.tokenValiditySeconds(3 * 24 * 60 * 60)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login/exit")
                );
        return http.build();
    }
}
