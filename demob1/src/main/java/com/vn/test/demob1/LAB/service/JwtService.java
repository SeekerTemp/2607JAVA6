package com.vn.test.demob1.LAB.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * LAB 5 - Tạo và kiểm tra JWT (theo hướng dẫn Bài 2 của Lab 6).
 */
@Service
public class JwtService {

    /** Thời hạn mặc định của token: 20 phút. */
    public static final int EXPIRY_SECONDS = 20 * 60;

    /**
     * Tạo JWT.
     * <p>
     * Mỗi token được gắn một mã định danh riêng (claim "jti") để có thể vô hiệu
     * hóa từng token khi đăng xuất - xem {@link TokenBlacklistService}.
     *
     * @param user          UserDetails chứa thông tin để tạo token
     * @param expirySeconds thời hạn hiệu lực tính bằng giây
     */
    public String create(UserDetails user, int expirySeconds) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(Map.of("name", "Poly"))
                .setId(UUID.randomUUID().toString())
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000L * expirySeconds))
                .signWith(this.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Bóc tách phần body (claims) từ JWT, đồng thời xác minh chữ ký.
     * Token sai chữ ký hoặc đã hết hạn đều ném ngoại lệ.
     */
    public Claims getBody(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Bóc tách claims kể cả khi token đã hết hạn (chữ ký vẫn phải đúng).
     * <p>
     * Dùng cho /jwt-decoder: đề bài yêu cầu in ra ngày hết hạn, nên token quá
     * hạn vẫn phải đọc được thay vì báo lỗi.
     */
    public Claims getBodyEvenIfExpired(String token) {
        try {
            return this.getBody(token);
        } catch (ExpiredJwtException ex) {
            return ex.getClaims();
        }
    }

    /**
     * Xác minh token còn trong thời gian hiệu lực.
     */
    public boolean validate(Claims claims) {
        return claims.getExpiration().after(new Date());
    }

    /**
     * Khóa ký dùng để ký và xác minh JWT (HS256 cần tối thiểu 32 byte).
     */
    private Key getSigningKey() {
        String secret = "0123456789.0123456789.0123456789";
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
