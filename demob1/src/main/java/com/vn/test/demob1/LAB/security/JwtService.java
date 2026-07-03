package com.vn.test.demob1.LAB.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * LAB 6 - Bài 2: cung cấp thao tác tạo (create) và kiểm tra (validate) JWT.
 */
@Service
public class JwtService {

    /**
     * Tạo JWT.
     *
     * @param user          UserDetails chứa thông tin để tạo token
     * @param expirySeconds thời hạn hiệu lực (giây)
     */
    public String create(UserDetails user, int expirySeconds) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(Map.of("name", "Poly"))
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000L * expirySeconds))
                .signWith(this.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Bóc tách phần body (claims) từ JWT (đồng thời xác minh chữ ký).
     */
    public Claims getBody(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Xác minh thời gian hiệu lực.
     */
    public boolean validate(Claims claims) {
        return claims.getExpiration().after(new Date());
    }

    /**
     * Tạo chữ ký số để ký và xác minh JWT.
     */
    private Key getSigningKey() {
        String secret = "0123456789.0123456789.0123456789"; // HS256 >= 32 byte
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
