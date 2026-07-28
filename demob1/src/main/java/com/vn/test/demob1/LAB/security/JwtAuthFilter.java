package com.vn.test.demob1.LAB.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * LAB 6 - Bài 2: bộ lọc chạy mỗi request để tự động đăng nhập từ JWT.
 *  - Bóc JWT từ header Authorization: Bearer ...
 *  - Kiểm tra JWT bằng JwtService (chữ ký + thời hạn)
 *  - Kiểm tra token đã bị đăng xuất chưa (ASM yêu cầu 6)
 *  - Thiết lập Authentication vào SecurityContext
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    /** Tên attribute chứa claims của token trên request hiện tại. */
    public static final String CLAIMS_ATTRIBUTE = "jwtClaims";

    @Autowired
    JwtService jwtService;

    @Autowired
    UserDetailsService userService;

    @Autowired
    TokenBlacklistService tokenBlacklist;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            var authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer ")) {
                try {
                    var token = authorization.substring(7).trim();
                    var claims = jwtService.getBody(token);
                    // Token đã đăng xuất thì coi như không hợp lệ
                    if (jwtService.validate(claims) && !tokenBlacklist.isRevoked(claims.getId())) {
                        var username = claims.getSubject();
                        UserDetails user = userService.loadUserByUsername(username);
                        var auth = new UsernamePasswordAuthenticationToken(
                                user, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        // Để controller /logout đọc lại claims (jti) mà không phải parse lần nữa
                        request.setAttribute(CLAIMS_ATTRIBUTE, claims);
                    }
                } catch (Exception ex) {
                    // Token không hợp lệ / hết hạn -> bỏ qua, coi như chưa đăng nhập
                    SecurityContextHolder.clearContext();
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
