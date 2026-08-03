package com.vn.test.demob1.LAB.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * LAB 5 - Kiểm thử 6 endpoint JWT.
 */
@SpringBootTest
@AutoConfigureMockMvc
class JwtApiTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper json;

    /** Đăng nhập thật và lấy chuỗi JWT. */
    private String login(String username, String password) throws Exception {
        String body = mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return json.readTree(body).get("jwt").asString();
    }

    private static String bearer(String jwt) {
        return "Bearer " + jwt;
    }

    // ---------- (1đ) GET /jwt-generator ----------

    @Test
    void jwtGeneratorReturnsToken() throws Exception {
        mvc.perform(get("/jwt-generator")
                        .param("username", "user@gmail.com")
                        .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user@gmail.com"))
                // JWT gồm 3 phần ngăn cách bởi dấu chấm
                .andExpect(jsonPath("$.jwt").value(org.hamcrest.Matchers.matchesRegex("[^.]+\\.[^.]+\\.[^.]+")));
    }

    // ---------- (1đ) GET /jwt-decoder/{jwt} ----------

    @Test
    void jwtDecoderReturnsUsernameAndExpiry() throws Exception {
        String jwt = login("admin@gmail.com", "123");

        mvc.perform(get("/jwt-decoder/" + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin@gmail.com"))
                .andExpect(jsonPath("$.expired").value(false))
                // dd/MM/yyyy HH:mm:ss
                .andExpect(jsonPath("$.expiration")
                        .value(org.hamcrest.Matchers.matchesRegex("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}")));
    }

    @Test
    void jwtDecoderRejectsGarbageToken() throws Exception {
        mvc.perform(get("/jwt-decoder/khong-phai-jwt"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Chuỗi JWT không hợp lệ!"));
    }

    // ---------- (2đ) POST /login ----------

    @Test
    void loginReturnsJwtForValidCredentials() throws Exception {
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"both@gmail.com\",\"password\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("both@gmail.com"))
                .andExpect(jsonPath("$.roles").value(
                        org.hamcrest.Matchers.containsInAnyOrder("ROLE_ADMIN", "ROLE_USER")))
                .andExpect(jsonPath("$.jwt").exists());
    }

    @Test
    void loginRejectsWrongPassword() throws Exception {
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user@gmail.com\",\"password\":\"sai\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Sai tên đăng nhập hoặc mật khẩu!"));
    }

    // ---------- (2đ) GET /user ----------

    @Test
    void userEndpointAllowsUserRole() throws Exception {
        mvc.perform(get("/user").header("Authorization", bearer(login("user@gmail.com", "123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user@gmail.com"));
    }

    @Test
    void userEndpointRejectsAdminOnlyAccount() throws Exception {
        // admin@gmail.com chỉ có ROLE_ADMIN nên không vào được /user
        mvc.perform(get("/user").header("Authorization", bearer(login("admin@gmail.com", "123"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void userEndpointRejectsRequestWithoutToken() throws Exception {
        mvc.perform(get("/user")).andExpect(status().isForbidden());
    }

    // ---------- (2đ) GET /admin ----------

    @Test
    void adminEndpointAllowsAdminRole() throws Exception {
        mvc.perform(get("/admin").header("Authorization", bearer(login("admin@gmail.com", "123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin@gmail.com"));
    }

    @Test
    void adminEndpointRejectsUserOnlyAccount() throws Exception {
        mvc.perform(get("/admin").header("Authorization", bearer(login("user@gmail.com", "123"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void accountWithBothRolesReachesUserAndAdmin() throws Exception {
        String jwt = login("both@gmail.com", "123");
        mvc.perform(get("/user").header("Authorization", bearer(jwt))).andExpect(status().isOk());
        mvc.perform(get("/admin").header("Authorization", bearer(jwt))).andExpect(status().isOk());
    }

    // ---------- (2đ) POST /logout ----------

    @Test
    void logoutRevokesTokenSoItCannotBeReused() throws Exception {
        String jwt = login("user@gmail.com", "123");

        // Trước khi đăng xuất: dùng được
        mvc.perform(get("/user").header("Authorization", bearer(jwt))).andExpect(status().isOk());

        mvc.perform(post("/logout").header("Authorization", bearer(jwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user@gmail.com"));

        // Sau khi đăng xuất: chính token đó bị từ chối
        mvc.perform(get("/user").header("Authorization", bearer(jwt)))
                .andExpect(status().isForbidden());
    }

    @Test
    void logoutOfOneTokenDoesNotAffectAnother() throws Exception {
        String jwt1 = login("user@gmail.com", "123");
        String jwt2 = login("user@gmail.com", "123");

        mvc.perform(post("/logout").header("Authorization", bearer(jwt1))).andExpect(status().isOk());

        // Token thứ hai của cùng tài khoản vẫn phải dùng được
        mvc.perform(get("/user").header("Authorization", bearer(jwt2))).andExpect(status().isOk());
    }

    @Test
    void logoutRequiresValidToken() throws Exception {
        mvc.perform(post("/logout")).andExpect(status().isForbidden());
    }

    // ---------- token do /jwt-generator sinh ra vẫn dùng được ----------

    @Test
    void tokenFromGeneratorWorksForAuthorizedEndpoint() throws Exception {
        String body = mvc.perform(get("/jwt-generator")
                        .param("username", "admin@gmail.com").param("password", "123"))
                .andReturn().getResponse().getContentAsString();
        String jwt = json.readTree(body).get("jwt").asString();

        // Vai trò được nạp lại từ danh sách tài khoản nên token này vào được /admin
        mvc.perform(get("/admin").header("Authorization", bearer(jwt)))
                .andExpect(status().isOk());
        assertThat(jwt).isNotBlank();
    }
}
