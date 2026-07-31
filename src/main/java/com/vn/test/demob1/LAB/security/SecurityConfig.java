package com.vn.test.demob1.LAB.security;

import com.vn.test.demob1.LAB.model.AppUser;
import com.vn.test.demob1.LAB.repository.AppUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
public class SecurityConfig {
//Config lab 2
    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder delegate = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return delegate.encode(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                if (encodedPassword == null) {
                    return false;
                }
                if (encodedPassword.startsWith("{")) {
                    return delegate.matches(rawPassword, encodedPassword);
                }
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder, AppUserRepository repository) {
        return username -> {
            Optional<AppUser> optionalUser = repository.findById(username);
            if (optionalUser.isEmpty()) {
                throw new UsernameNotFoundException("Không tìm thấy người dùng: " + username);
            }

            AppUser appUser = optionalUser.get();
            return User.withUsername(appUser.getUsername())
                    .password(appUser.getPassword())
                    .roles(appUser.getRole())
                    .disabled(!appUser.isEnabled())
                    .build();
        };
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
