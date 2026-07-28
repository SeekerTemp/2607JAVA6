# ASM LAB 6 - REST API bảo mật bằng JWT

Nhánh: `asm/lab6` (tách từ `lab/lab6`). Server chạy tại **http://localhost:8080**.

## 1. Chạy ứng dụng

```bash
cd demob1
./mvnw spring-boot:run          # Windows CMD: mvnw.cmd spring-boot:run
```

Chạy bộ kiểm thử tự động (15 test, phủ cả 6 yêu cầu):

```bash
./mvnw test
```

Trang demo bằng Axios (bấm nút để gọi lần lượt 6 API):
**http://localhost:8080/asm-jwt-client.html**

## 2. Tài khoản (InMemory, không dùng CSDL)

| USERNAME        | PASSWORD | ROLES        |
|-----------------|----------|--------------|
| user@gmail.com  | 123      | USER         |
| admin@gmail.com | 123      | ADMIN        |
| both@gmail.com  | 123      | USER, ADMIN  |

## 3. Cách gọi từng API

### (1đ) GET /jwt-generator - tạo JWT từ username + password

```bash
curl "http://localhost:8080/jwt-generator?username=user@gmail.com&password=123"
```
```json
{"token":"eyJhbGciOiJIUzI1NiJ9...","username":"user@gmail.com","expiration":"28/07/2026 13:57:34"}
```
- Postman: `GET`, tab **Params** thêm `username`, `password`.
- Đây là công cụ minh họa cách **tạo** token nên không kiểm tra tài khoản (việc xác thực là của `POST /login`).
- Mật khẩu **không** được ghi vào payload: payload JWT chỉ là Base64, ai đọc được token là đọc được mật khẩu.

### (1đ) GET /jwt-decoder/{jwt} - đọc tên tài khoản + ngày hết hạn

```bash
TOKEN=<chuỗi jwt>
curl "http://localhost:8080/jwt-decoder/$TOKEN"
```
```json
{"username":"user@gmail.com","expiration":"28/07/2026 13:57:34",
 "issuedAt":"28/07/2026 13:37:34","expired":false,"revoked":false}
```
- Token sai chữ ký → `400` + `{"error":"JWT không hợp lệ: ..."}`.
- Token hết hạn vẫn đọc được thông tin, kèm `"expired": true`.

### (2đ) POST /login - xác thực rồi trả về JWT

```bash
curl -X POST http://localhost:8080/login \
     -H "Content-Type: application/json" \
     -d '{"username":"user@gmail.com","password":"123"}'
```
```json
{"token":"eyJ...","username":"user@gmail.com","roles":["ROLE_USER"],"expiration":"28/07/2026 13:57:35"}
```
- Postman: `POST`, **Body → raw → JSON** như trên.
- Sai username/password → `401` + `{"error":"Sai tên đăng nhập hoặc mật khẩu"}`.
- Token có hiệu lực **20 phút**.

### (2đ) GET /user - chỉ vai trò USER

```bash
curl http://localhost:8080/user -H "Authorization: Bearer $TOKEN"
```
```json
{"url":"/user","message":"Xin chào người dùng!","username":"user@gmail.com","roles":["ROLE_USER"]}
```
- Postman: tab **Authorization → Bearer Token**, hoặc **Headers**: `Authorization: Bearer <jwt>`.
- Không gửi JWT / JWT sai / JWT đã logout → `401`.
- JWT của `admin@gmail.com` (không có vai trò USER) → `403`.

### (2đ) GET /admin - chỉ vai trò ADMIN

```bash
curl http://localhost:8080/admin -H "Authorization: Bearer $TOKEN"
```
- JWT của `user@gmail.com` → `403`; không có JWT → `401`.
- `both@gmail.com` có cả 2 vai trò nên vào được cả `/user` và `/admin`.

### (2đ) POST /logout - vô hiệu hóa JWT

```bash
curl -X POST http://localhost:8080/logout -H "Authorization: Bearer $TOKEN"
```
```json
{"message":"Đăng xuất thành công, JWT đã bị vô hiệu hóa",
 "username":"user@gmail.com","jti":"c2933b91-7287-4f18-a2f5-c944b05f8149"}
```
Kiểm tra token đã "chết":

```bash
curl -i http://localhost:8080/user -H "Authorization: Bearer $TOKEN"   # -> 401
```

## 4. Cách vô hiệu hóa JWT hoạt động

JWT là **stateless**: server không lưu token nên không thể "xóa" token đã phát hành, và token vẫn
đúng chữ ký cho tới khi hết hạn. Cách xử lý trong bài:

1. `JwtService.create()` gắn cho mỗi token một mã định danh duy nhất - claim **`jti`**.
2. `POST /logout` đưa `jti` của token đang dùng vào `TokenBlacklistService` (danh sách trong bộ nhớ).
3. `JwtAuthFilter` mỗi request đều kiểm tra `jti` có nằm trong danh sách hay không; nếu có thì
   **không** thiết lập Authentication → request bị trả `401`.
4. Các token khác của cùng tài khoản không bị ảnh hưởng (chỉ chặn đúng token đã đăng xuất).

Danh sách nằm trong RAM nên **mất khi khởi động lại server**; hệ thống thật nên dùng Redis/CSDL với
TTL bằng thời hạn còn lại của token. Các `jti` đã quá hạn được tự động dọn để không phình bộ nhớ.

## 5. Các file liên quan

| File | Vai trò |
|------|---------|
| `LAB/rest/AsmJwtApi.java` | 6 endpoint của ASM |
| `LAB/security/JwtService.java` | tạo / giải mã / kiểm tra JWT (thêm `jti`) |
| `LAB/security/TokenBlacklistService.java` | danh sách JWT đã đăng xuất |
| `LAB/security/JwtAuthFilter.java` | tự động đăng nhập từ JWT + chặn token đã logout |
| `LAB/security/SecurityConfig.java` | phân quyền URL, user InMemory, lỗi 401/403 dạng JSON |
| `static/asm-jwt-client.html` | trang demo bằng Axios |
| `src/test/java/.../AsmJwtApiTests.java` | 15 test kiểm thử tự động |

Các API của LAB 6 gốc (`/poly/url0..url4`, `POST /poly/login`) vẫn giữ nguyên.

## 6. Ghi chú kỹ thuật

- `SecurityConfig` phải gọi `http.logout(logout -> logout.disable())`: mặc định Spring Security đăng ký
  `LogoutFilter` chiếm sẵn `POST /logout`, làm controller không bao giờ được gọi.
- `spring-boot-starter-jackson` được thêm vào `pom.xml` để REST API có hỗ trợ JSON tường minh
  (trước đó Jackson chỉ vô tình có mặt do phụ thuộc `jjwt-jackson` scope `runtime`).
- `src/test/resources/application.properties` tắt autoconfigure của DataSource/JPA: các starter
  `*-test` kéo theo autoconfigure CSDL trong khi bài lab không dùng CSDL, khiến
  ApplicationContext không khởi động được khi chạy test.
