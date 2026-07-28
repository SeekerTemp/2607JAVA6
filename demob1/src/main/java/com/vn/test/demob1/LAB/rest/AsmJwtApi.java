package com.vn.test.demob1.LAB.rest;

import com.vn.test.demob1.LAB.security.JwtAuthFilter;
import com.vn.test.demob1.LAB.security.JwtService;
import com.vn.test.demob1.LAB.security.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ASM LAB 6 - REST API sử dụng JWT.
 *
 * <pre>
 * GET  /jwt-generator?username=..&password=..  -> chuỗi JWT tương ứng          (1đ)
 * GET  /jwt-decoder/{jwt}                      -> tên tài khoản + ngày hết hạn (1đ)
 * POST /login   {username, password}           -> xác thực, trả về JWT         (2đ)
 * GET  /user    Authorization: Bearer &lt;jwt&gt;    -> chỉ vai trò USER            (2đ)
 * GET  /admin   Authorization: Bearer &lt;jwt&gt;    -> chỉ vai trò ADMIN           (2đ)
 * POST /logout  Authorization: Bearer &lt;jwt&gt;    -> vô hiệu hóa JWT             (2đ)
 * </pre>
 *
 * Phân quyền cho các URL trên được khai báo tại
 * {@link com.vn.test.demob1.LAB.security.SecurityConfig}.
 */
@RestController
public class AsmJwtApi {

    /** Thời hạn hiệu lực của token: 20 phút. */
    private static final int EXPIRY_SECONDS = 20 * 60;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault());

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    TokenBlacklistService tokenBlacklist;

    /**
     * Yêu cầu 1: sinh JWT từ username/password truyền vào.
     * <p>
     * Đây là công cụ minh họa cách tạo token nên không kiểm tra tài khoản
     * (việc xác thực là của POST /login). Mật khẩu chỉ dùng làm tham số đầu vào,
     * KHÔNG được đưa vào payload vì payload của JWT chỉ là Base64 - ai đọc được
     * token là đọc được mật khẩu.
     */
    @GetMapping("/jwt-generator")
    public Object generate(@RequestParam String username, @RequestParam String password) {
        if (username.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("username và password không được để trống");
        }
        String token = jwtService.create(this.asUserDetails(username), EXPIRY_SECONDS);

        var result = new LinkedHashMap<String, Object>();
        result.put("token", token);
        result.put("username", username);
        result.put("expiration", this.format(jwtService.getBody(token).getExpiration()));
        return result;
    }

    /**
     * Yêu cầu 2: giải mã JWT, trả về tên tài khoản và ngày hết hạn.
     * <p>
     * Token sai chữ ký -> 400. Token hết hạn vẫn đọc được thông tin,
     * kèm cờ "expired" = true.
     */
    @GetMapping("/jwt-decoder/{jwt}")
    public Object decode(@PathVariable("jwt") String jwt) {
        Claims claims = jwtService.getBodyEvenIfExpired(jwt);

        var result = new LinkedHashMap<String, Object>();
        result.put("username", claims.getSubject());
        result.put("expiration", this.format(claims.getExpiration()));
        result.put("issuedAt", this.format(claims.getIssuedAt()));
        result.put("expired", !jwtService.validate(claims));
        result.put("revoked", tokenBlacklist.isRevoked(claims.getId()));
        return result;
    }

    /**
     * Yêu cầu 3: đăng nhập. Thông tin hợp lệ thì trả về JWT, sai thì 401.
     */
    @PostMapping("/login")
    public Object login(@RequestBody Map<String, String> userInfo) {
        String username = userInfo.get("username");
        String password = userInfo.get("password");

        var authInfo = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authInfo);

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String token = jwtService.create(user, EXPIRY_SECONDS);

        var result = new LinkedHashMap<String, Object>();
        result.put("token", token);
        result.put("username", user.getUsername());
        result.put("roles", this.rolesOf(user.getAuthorities()));
        result.put("expiration", this.format(jwtService.getBody(token).getExpiration()));
        return result;
    }

    /**
     * Yêu cầu 4: chỉ tài khoản có vai trò USER truy xuất được.
     */
    @GetMapping("/user")
    public Object userArea(Authentication authentication) {
        return this.area("/user", "Xin chào người dùng!", authentication);
    }

    /**
     * Yêu cầu 5: chỉ tài khoản có vai trò ADMIN truy xuất được.
     */
    @GetMapping("/admin")
    public Object adminArea(Authentication authentication) {
        return this.area("/admin", "Xin chào quản trị viên!", authentication);
    }

    /**
     * Yêu cầu 6: vô hiệu hóa JWT đang dùng - các request sau mang token này sẽ bị từ chối.
     */
    @PostMapping("/logout")
    public Object logout(HttpServletRequest request, Authentication authentication) {
        Claims claims = (Claims) request.getAttribute(JwtAuthFilter.CLAIMS_ATTRIBUTE);
        if (claims == null) {
            throw new IllegalArgumentException(
                    "Thiếu JWT. Gửi kèm header: Authorization: Bearer <jwt>");
        }
        if (!tokenBlacklist.revoke(claims)) {
            throw new IllegalArgumentException("JWT không có mã định danh (jti), không thể vô hiệu hóa");
        }

        var result = new LinkedHashMap<String, Object>();
        result.put("message", "Đăng xuất thành công, JWT đã bị vô hiệu hóa");
        result.put("username", authentication.getName());
        result.put("jti", claims.getId());
        return result;
    }

    private Object area(String url, String message, Authentication authentication) {
        var result = new LinkedHashMap<String, Object>();
        result.put("url", url);
        result.put("message", message);
        result.put("username", authentication.getName());
        result.put("roles", this.rolesOf(authentication.getAuthorities()));
        return result;
    }

    /**
     * JwtService.create() cần một UserDetails; ở /jwt-generator ta chỉ có username.
     * Vai trò không lấy từ token mà được nạp lại từ UserDetailsService ở mỗi request
     * (xem JwtAuthFilter), nên UserDetails tạm này không cần quyền.
     */
    private UserDetails asUserDetails(String username) {
        return User.withUsername(username)
                .password("")
                .authorities(Collections.<GrantedAuthority>emptyList())
                .build();
    }

    private List<String> rolesOf(java.util.Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).sorted().toList();
    }

    private String format(Date date) {
        return date == null ? null : DATE_FORMAT.format(date.toInstant());
    }

    /** Sai username/password -> 401 kèm thông báo JSON. */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleBadCredentials(AuthenticationException ex) {
        return this.error(HttpStatus.UNAUTHORIZED, "Sai tên đăng nhập hoặc mật khẩu");
    }

    /** JWT sai định dạng / sai chữ ký -> 400. */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handleInvalidJwt(JwtException ex) {
        return this.error(HttpStatus.BAD_REQUEST, "JWT không hợp lệ: " + ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        return this.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<Object> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("error", message));
    }
}
