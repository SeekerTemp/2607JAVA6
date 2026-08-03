package com.vn.test.demob1.LAB.controller;

import com.vn.test.demob1.LAB.model.LoginRequest;
import com.vn.test.demob1.LAB.security.JwtAuthFilter;
import com.vn.test.demob1.LAB.service.JwtService;
import com.vn.test.demob1.LAB.service.TokenBlacklistService;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * LAB 5 - REST API sử dụng JWT (kiểm thử bằng Postman).
 *
 * <pre>
 * GET  /jwt-generator?username=..&amp;password=..  -> chuỗi JWT tương ứng          (1đ)
 * GET  /jwt-decoder/{jwt}                       -> tên tài khoản + ngày hết hạn (1đ)
 * POST /login   {username, password}            -> xác thực, trả về JWT         (2đ)
 * GET  /user    Authorization: Bearer &lt;jwt&gt;     -> chỉ vai người dùng          (2đ)
 * GET  /admin   Authorization: Bearer &lt;jwt&gt;     -> chỉ vai quản trị viên       (2đ)
 * POST /logout  Authorization: Bearer &lt;jwt&gt;     -> vô hiệu hóa JWT             (2đ)
 * </pre>
 *
 * Phân quyền của /user, /admin, /logout khai báo tại
 * {@link com.vn.test.demob1.LAB.security.SecurityConfig}.
 */
@RestController
public class JwtController {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault());

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    TokenBlacklistService tokenBlacklist;

    /**
     * (1đ) Sinh JWT từ username và password truyền vào.
     * <p>
     * Cả hai tham số đều bắt buộc; thiếu một tham số trả về 400, sai tài khoản
     * hoặc mật khẩu trả về 401.
     */
    @GetMapping("/jwt-generator")
    public Object generate(@RequestParam("username") String username,
                           @RequestParam("password") String password) {
        UserDetails user = this.authenticate(username, password);
        return this.tokenResponse(user);
    }

    /**
     * (1đ) Giải mã JWT, trả về tên tài khoản và ngày hết hạn.
     * <p>
     * {jwt:.+} để Spring không cắt mất phần sau dấu chấm cuối cùng của token.
     * Token hết hạn vẫn đọc được (chỉ cần đúng chữ ký) vì đề bài yêu cầu in ra
     * ngày hết hạn.
     */
    @GetMapping("/jwt-decoder/{jwt:.+}")
    public Object decode(@PathVariable("jwt") String jwt) {
        Claims claims = jwtService.getBodyEvenIfExpired(jwt);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("username", claims.getSubject());
        result.put("expiration", DATE_FORMAT.format(claims.getExpiration().toInstant()));
        result.put("expired", !jwtService.validate(claims));
        return result;
    }

    /**
     * (2đ) Xác thực username/password, hợp lệ thì trả về JWT.
     */
    @PostMapping("/login")
    public Object login(@RequestBody LoginRequest request) {
        UserDetails user = this.authenticate(request.getUsername(), request.getPassword());
        return this.tokenResponse(user);
    }

    /**
     * (2đ) Chỉ cho phép tài khoản có vai người dùng (USER).
     */
    @GetMapping("/user")
    public Object user(Authentication authentication) {
        return greeting("Xin chào người dùng", authentication);
    }

    /**
     * (2đ) Chỉ cho phép tài khoản có vai quản trị viên (ADMIN).
     */
    @GetMapping("/admin")
    public Object admin(Authentication authentication) {
        return greeting("Xin chào quản trị viên", authentication);
    }

    /**
     * (2đ) Vô hiệu hóa JWT đang dùng, các request sau mang token này sẽ bị từ chối.
     */
    @PostMapping("/logout")
    public Object logout(HttpServletRequest request, Authentication authentication) {
        Claims claims = (Claims) request.getAttribute(JwtAuthFilter.CLAIMS_ATTRIBUTE);
        tokenBlacklist.revoke(claims);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Đã đăng xuất, chuỗi JWT này không dùng được nữa");
        result.put("username", authentication.getName());
        return result;
    }

    // ---------- hỗ trợ ----------

    /**
     * Xác thực username/password với danh sách tài khoản.
     * Sai thông tin sẽ ném {@link AuthenticationException} -> 401.
     */
    private UserDetails authenticate(String username, String password) {
        var authInfo = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authInfo);
        return (UserDetails) authentication.getPrincipal();
    }

    /** Phản hồi chung cho /jwt-generator và /login. */
    private Object tokenResponse(UserDetails user) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("username", user.getUsername());
        result.put("roles", roleNames(user));
        result.put("jwt", jwtService.create(user, JwtService.EXPIRY_SECONDS));
        return result;
    }

    private Object greeting(String message, Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", message);
        result.put("username", user.getUsername());
        result.put("roles", roleNames(user));
        return result;
    }

    private List<String> roleNames(UserDetails user) {
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .sorted()
                .toList();
    }

    /** Sai tài khoản hoặc mật khẩu khi gọi /jwt-generator hoặc /login. */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthError(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Sai tên đăng nhập hoặc mật khẩu!"));
    }

    /** Chuỗi JWT không hợp lệ khi gọi /jwt-decoder. */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handleJwtError(JwtException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Chuỗi JWT không hợp lệ!"));
    }
}
