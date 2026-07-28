package com.vn.test.demob1.LAB.security;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ASM LAB 6 - Yêu cầu 6: danh sách JWT đã bị vô hiệu hóa (đăng xuất).
 * <p>
 * JWT là stateless nên bản thân server không thể "xóa" một token đã phát hành.
 * Cách xử lý: lưu lại mã định danh (claim "jti") của các token đã đăng xuất,
 * {@link JwtAuthFilter} sẽ từ chối mọi request mang token nằm trong danh sách này.
 * <p>
 * Danh sách lưu trong bộ nhớ (mất khi khởi động lại server) - đủ cho bài lab;
 * hệ thống thật nên dùng Redis/CSDL với TTL bằng thời hạn còn lại của token.
 */
@Service
public class TokenBlacklistService {

    /** jti -> thời điểm token hết hạn (dùng để dọn rác) */
    private final Map<String, Date> revoked = new ConcurrentHashMap<>();

    /**
     * Vô hiệu hóa token.
     *
     * @param claims body của token cần vô hiệu hóa
     * @return false nếu token không có claim "jti" (không thể vô hiệu hóa riêng lẻ)
     */
    public boolean revoke(Claims claims) {
        String jti = claims.getId();
        if (jti == null || jti.isBlank()) {
            return false;
        }
        Date expiration = claims.getExpiration() != null ? claims.getExpiration() : new Date();
        revoked.put(jti, expiration);
        this.purgeExpired();
        return true;
    }

    /**
     * Token đã bị vô hiệu hóa chưa?
     *
     * @param jti mã định danh token, có thể null
     */
    public boolean isRevoked(String jti) {
        return jti != null && revoked.containsKey(jti);
    }

    /** Số token đang bị chặn (phục vụ kiểm thử / theo dõi). */
    public int size() {
        return revoked.size();
    }

    /**
     * Token đã quá hạn thì tự nó không dùng được nữa, không cần giữ trong danh sách.
     */
    private void purgeExpired() {
        Date now = new Date();
        revoked.values().removeIf(expiration -> expiration.before(now));
    }
}
