package com.vn.test.demob1.LAB.security;

import com.vn.test.demob1.LAB.service.JwtService;
import com.vn.test.demob1.LAB.service.TokenBlacklistService;
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
 * LAB 5 - Bộ lọc chạy mỗi request để tự động đăng nhập từ JWT:
 * <ul>
 *   <li>Bóc JWT từ header {@code Authorization: Bearer ...}</li>
 *   <li>Kiểm tra chữ ký và thời hạn bằng {@link JwtService}</li>
 *   <li>Kiểm tra token đã bị đăng xuất chưa ({@link TokenBlacklistService})</li>
 *   <li>Thiết lập Authentication vào SecurityContext</li>
 * </ul>
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
            String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer ")) {
                try {
                    String token = authorization.substring(7).trim();
                    var claims = jwtService.getBody(token);
                    // Token đã đăng xuất thì coi như không hợp lệ
                    if (jwtService.validate(claims) && !tokenBlacklist.isRevoked(claims.getId())) {
                        UserDetails user = userService.loadUserByUsername(claims.getSubject());
                        var auth = new UsernamePasswordAuthenticationToken(
                                user, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        // Để /logout đọc lại jti mà không phải parse token lần nữa
                        request.setAttribute(CLAIMS_ATTRIBUTE, claims);
                    }
                } catch (Exception ex) {
                    // Token hỏng / sai chữ ký / hết hạn -> coi như chưa đăng nhập
                    SecurityContextHolder.clearContext();
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
