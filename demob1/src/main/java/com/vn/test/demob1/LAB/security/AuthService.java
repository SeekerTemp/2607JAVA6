package com.vn.test.demob1.LAB.security;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
    @Service
    public class AuthService {

        public Authentication getAuthentication() {
            return SecurityContextHolder
                    .getContext()
                    .getAuthentication();
        }

        public String getEmail() {

            Authentication auth = getAuthentication();

            if (auth == null || !auth.isAuthenticated()) {
                return "Guest";
            }

            return auth.getName();
        }

        public boolean isLogin() {

            Authentication auth = getAuthentication();

            return auth != null
                    && auth.isAuthenticated()
                    && !"anonymousUser".equals(auth.getName());
        }
}
