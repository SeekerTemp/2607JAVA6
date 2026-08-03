package com.vn.test.demob1.LAB.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LAB 5 - Yêu cầu /logout: danh sách JWT đã bị vô hiệu hóa.
 * <p>
 * JWT là stateless nên server không thể "xóa" một token đã phát hành. Cách xử
 * lý: lưu lại mã định danh (claim "jti") của những token đã đăng xuất;
 * bộ lọc {@code JwtAuthFilter} sẽ từ chối mọi request mang token trong danh sách.
 * <p>
 * Danh sách nằm trong bộ nhớ nên sẽ mất khi khởi động lại server - đủ dùng cho
 * bài lab vì tài khoản cũng khai báo InMemory.
 */
@Service
public class TokenBlacklistService {

    /** jti -> thời điểm token hết hạn, dùng để dọn rác. */
    private final Map<String, Date> revoked = new ConcurrentHashMap<>();

    /**
     * Vô hiệu hóa một token.
     *
     * @param claims body của token cần thu hồi
     */
    public void revoke(Claims claims) {
        if (claims != null && claims.getId() != null) {
            revoked.put(claims.getId(), claims.getExpiration());
            this.removeExpired();
        }
    }

    /**
     * Token đã bị đăng xuất hay chưa.
     *
     * @param jti mã định danh của token
     */
    public boolean isRevoked(String jti) {
        return jti != null && revoked.containsKey(jti);
    }

    public int size() {
        return revoked.size();
    }

    /**
     * Token đã quá hạn thì tự nó vô hiệu, không cần giữ trong danh sách nữa.
     */
    private void removeExpired() {
        Date now = new Date();
        revoked.entrySet().removeIf(e -> e.getValue() != null && e.getValue().before(now));
    }
}
