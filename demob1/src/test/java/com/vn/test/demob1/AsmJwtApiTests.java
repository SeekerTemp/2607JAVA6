package com.vn.test.demob1;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ASM LAB 6 - kiểm thử 6 yêu cầu của REST API JWT.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AsmJwtApiTests {

    @Autowired
    MockMvc mvc;

    // ---------- Yêu cầu 1: GET /jwt-generator ----------

    @Test
    void jwtGenerator_traVeChuoiJwt() throws Exception {
        MvcResult result = mvc.perform(get("/jwt-generator")
                        .param("username", "user@gmail.com")
                        .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user@gmail.com"))
                .andReturn();

        String token = this.read(result, "token");
        assertThat(token.split("\\.")).hasSize(3); // header.payload.signature
    }

    @Test
    void jwtGenerator_thieuThamSo_thiBaoLoi() throws Exception {
        mvc.perform(get("/jwt-generator").param("username", "user@gmail.com"))
                .andExpect(status().isBadRequest());
    }

    // ---------- Yêu cầu 2: GET /jwt-decoder/{jwt} ----------

    @Test
    void jwtDecoder_traVeTenTaiKhoanVaNgayHetHan() throws Exception {
        String token = this.login("user@gmail.com", "123");

        mvc.perform(get("/jwt-decoder/{jwt}", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user@gmail.com"))
                .andExpect(jsonPath("$.expired").value(false))
                // dd/MM/yyyy HH:mm:ss
                .andExpect(jsonPath("$.expiration").value(
                        org.hamcrest.Matchers.matchesPattern("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}")));
    }

    @Test
    void jwtDecoder_tokenSaiChuKy_thiBaoLoi() throws Exception {
        String token = this.login("user@gmail.com", "123");
        String forged = token.substring(0, token.lastIndexOf('.')) + ".chu-ky-gia-mao";

        mvc.perform(get("/jwt-decoder/{jwt}", forged))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    // ---------- Yêu cầu 3: POST /login ----------

    @Test
    void login_dungThongTin_traVeJwt() throws Exception {
        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"both@gmail.com\",\"password\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("both@gmail.com"))
                .andExpect(jsonPath("$.roles").value(
                        org.hamcrest.Matchers.containsInAnyOrder("ROLE_ADMIN", "ROLE_USER")));
    }

    @Test
    void login_saiMatKhau_thi401() throws Exception {
        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user@gmail.com\",\"password\":\"sai-mat-khau\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").exists());
    }

    // ---------- Yêu cầu 4 & 5: GET /user, GET /admin ----------

    @Test
    void user_chiChoVaiTroUser() throws Exception {
        mvc.perform(get("/user").header("Authorization", "Bearer " + this.login("user@gmail.com", "123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user@gmail.com"));

        // ADMIN không phải USER -> 403
        mvc.perform(get("/user").header("Authorization", "Bearer " + this.login("admin@gmail.com", "123")))
                .andExpect(status().isForbidden());

        // Không có JWT -> 401
        mvc.perform(get("/user")).andExpect(status().isUnauthorized());
    }

    @Test
    void admin_chiChoVaiTroAdmin() throws Exception {
        mvc.perform(get("/admin").header("Authorization", "Bearer " + this.login("admin@gmail.com", "123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin@gmail.com"));

        mvc.perform(get("/admin").header("Authorization", "Bearer " + this.login("user@gmail.com", "123")))
                .andExpect(status().isForbidden());

        mvc.perform(get("/admin")).andExpect(status().isUnauthorized());
    }

    @Test
    void taiKhoanCaHaiVaiTro_vaoDuocCaUserVaAdmin() throws Exception {
        String token = this.login("both@gmail.com", "123");

        mvc.perform(get("/user").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        mvc.perform(get("/admin").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void jwtGiaMao_khongDangNhapDuoc() throws Exception {
        String token = this.login("user@gmail.com", "123");
        String forged = token.substring(0, token.lastIndexOf('.')) + ".chu-ky-gia-mao";

        mvc.perform(get("/user").header("Authorization", "Bearer " + forged))
                .andExpect(status().isUnauthorized());
    }

    // ---------- Yêu cầu 6: POST /logout ----------

    @Test
    void logout_thiJwtKhongConDungDuoc() throws Exception {
        String token = this.login("user@gmail.com", "123");

        // Trước khi đăng xuất: dùng được
        mvc.perform(get("/user").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mvc.perform(post("/logout").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user@gmail.com"))
                .andExpect(jsonPath("$.jti").exists());

        // Sau khi đăng xuất: token bị từ chối
        mvc.perform(get("/user").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());

        // Đăng xuất lần nữa với token đã vô hiệu hóa -> cũng bị từ chối
        mvc.perform(post("/logout").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());

        // /jwt-decoder vẫn đọc được và cho biết token đã bị vô hiệu hóa
        mvc.perform(get("/jwt-decoder/{jwt}", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.revoked").value(true));
    }

    @Test
    void logout_khongCoJwt_thi401() throws Exception {
        mvc.perform(post("/logout")).andExpect(status().isUnauthorized());
    }

    @Test
    void logout_khongAnhHuongTokenKhac() throws Exception {
        String token1 = this.login("user@gmail.com", "123");
        String token2 = this.login("user@gmail.com", "123");

        mvc.perform(post("/logout").header("Authorization", "Bearer " + token1))
                .andExpect(status().isOk());

        mvc.perform(get("/user").header("Authorization", "Bearer " + token1))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/user").header("Authorization", "Bearer " + token2))
                .andExpect(status().isOk());
    }

    // ---------- LAB 6 (Bài 1 & 3) vẫn hoạt động ----------

    @Test
    void polyApi_vanGiuNguyenPhanQuyenCuaLab() throws Exception {
        mvc.perform(get("/poly/url0")).andExpect(status().isOk());
        mvc.perform(get("/poly/url1")).andExpect(status().isUnauthorized());
        mvc.perform(get("/poly/url3")
                        .header("Authorization", "Bearer " + this.login("admin@gmail.com", "123")))
                .andExpect(status().isOk());
        mvc.perform(get("/poly/url2")
                        .header("Authorization", "Bearer " + this.login("admin@gmail.com", "123")))
                .andExpect(status().isForbidden());
    }

    // ---------- helper ----------

    private String login(String username, String password) throws Exception {
        MvcResult result = mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn();
        return this.read(result, "token");
    }

    private String read(MvcResult result, String field) throws Exception {
        return JsonPath.read(result.getResponse().getContentAsString(), "$." + field);
    }
}
