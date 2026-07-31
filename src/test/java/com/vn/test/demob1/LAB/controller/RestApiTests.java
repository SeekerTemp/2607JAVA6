package com.vn.test.demob1.LAB.controller;

import com.vn.test.demob1.LAB.repository.AppUserRepository;
import com.vn.test.demob1.LAB.repository.CategoryRepository;
import com.vn.test.demob1.LAB.repository.ProductRepository;
import com.vn.test.demob1.LAB.model.AppUser;
import com.vn.test.demob1.LAB.model.Category;
import com.vn.test.demob1.LAB.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * LAB 7 - Kiểm thử 3 REST API: loại hàng, sản phẩm, người sử dụng.
 */
@SpringBootTest
@AutoConfigureMockMvc
class RestApiTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper json;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @BeforeEach
    void clean() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        appUserRepository.deleteAll();
    }

    // ---------- Bài 1: loại hàng ----------

    @Test
    void categoryCrud() throws Exception {
        mvc.perform(post("/categories").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(new Category("C01", "Điện thoại"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Điện thoại"));

        mvc.perform(get("/categories")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mvc.perform(put("/categories/C01").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(new Category("C01", "Mobile"))))
                .andExpect(status().isOk());
        assertThat(categoryRepository.findById("C01").orElseThrow().getName()).isEqualTo("Mobile");

        mvc.perform(delete("/categories/C01")).andExpect(status().isOk());
        assertThat(categoryRepository.count()).isZero();
    }

    @Test
    void categoryRejectsDuplicateIdAndBlankName() throws Exception {
        categoryRepository.save(new Category("C01", "Điện thoại"));

        mvc.perform(post("/categories").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(new Category("C01", "Trùng mã"))))
                .andExpect(status().isConflict());
        // Bản ghi cũ không bị ghi đè
        assertThat(categoryRepository.findById("C01").orElseThrow().getName()).isEqualTo("Điện thoại");

        mvc.perform(post("/categories").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(new Category("C02", " "))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void categoryUpdateKeepsIdFromUrl() throws Exception {
        categoryRepository.save(new Category("C01", "Điện thoại"));

        // Form đổi mã -> vẫn chỉ sửa C01, không sinh thêm dòng C99
        mvc.perform(put("/categories/C01").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(new Category("C99", "Đổi tên"))))
                .andExpect(status().isOk());
        assertThat(categoryRepository.count()).isEqualTo(1);
        assertThat(categoryRepository.findById("C01").orElseThrow().getName()).isEqualTo("Đổi tên");
    }

    @Test
    void categoryDeleteBlockedWhenProductsReferenceIt() throws Exception {
        categoryRepository.save(new Category("C01", "Điện thoại"));
        productRepository.save(new Product("P01", "iPhone", 100, LocalDate.of(2026, 1, 10), "C01"));

        mvc.perform(delete("/categories/C01")).andExpect(status().isConflict());
        assertThat(categoryRepository.existsById("C01")).isTrue();
    }

    // ---------- Bài 2: sản phẩm ----------

    @Test
    void productCrudWithCategoryForeignKey() throws Exception {
        categoryRepository.save(new Category("C01", "Điện thoại"));
        Product p = new Product("P01", "iPhone 15", 25000000, LocalDate.of(2026, 1, 10), "C01");

        mvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(p)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2026-01-10"))
                .andExpect(jsonPath("$.categoryId").value("C01"));

        p.setPrice(24000000);
        mvc.perform(put("/products/P01").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(p)))
                .andExpect(status().isOk());
        assertThat(productRepository.findById("P01").orElseThrow().getPrice()).isEqualTo(24000000);

        mvc.perform(delete("/products/P01")).andExpect(status().isOk());
        assertThat(productRepository.count()).isZero();
    }

    @Test
    void productRejectsUnknownCategoryAndBadFields() throws Exception {
        mvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(
                                new Product("P01", "iPhone", 100, LocalDate.now(), "KHONG-CO"))))
                .andExpect(status().isBadRequest());

        categoryRepository.save(new Category("C01", "Điện thoại"));

        // Giá <= 0
        mvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(
                                new Product("P01", "iPhone", 0, LocalDate.now(), "C01"))))
                .andExpect(status().isBadRequest());

        // Ngày nhập để trống
        mvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(
                                new Product("P01", "iPhone", 100, null, "C01"))))
                .andExpect(status().isBadRequest());

        assertThat(productRepository.count()).isZero();
    }

    @Test
    void productAcceptsNullDateJsonFromEmptyDateInput() throws Exception {
        // Ô <input type="date"> để trống được gửi thành null chứ không phải chuỗi rỗng
        categoryRepository.save(new Category("C01", "Điện thoại"));
        String body = "{\"id\":\"P01\",\"name\":\"iPhone\",\"price\":100,\"date\":null,\"categoryId\":\"C01\"}";

        mvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    // ---------- Bài 3: người sử dụng ----------

    @Test
    void appUserCrud() throws Exception {
        AppUser u = new AppUser("user@gmail.com", "123", "Người dùng", true, "USER");

        mvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(u)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("USER"));

        u.setRole("ADMIN");
        mvc.perform(put("/accounts/user@gmail.com").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(u)))
                .andExpect(status().isOk());
        assertThat(appUserRepository.findById("user@gmail.com").orElseThrow().getRole()).isEqualTo("ADMIN");

        mvc.perform(delete("/accounts/user@gmail.com")).andExpect(status().isOk());
        assertThat(appUserRepository.count()).isZero();
    }

    @Test
    void appUserRejectsInvalidRoleAndDuplicateUsername() throws Exception {
        mvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(
                                new AppUser("u1", "123", "Tên", true, "MANAGER"))))
                .andExpect(status().isBadRequest());

        appUserRepository.save(new AppUser("u1", "123", "Tên", true, "USER"));
        mvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(
                                new AppUser("u1", "456", "Tên khác", true, "ADMIN"))))
                .andExpect(status().isConflict());
        assertThat(appUserRepository.findById("u1").orElseThrow().getFullname()).isEqualTo("Tên");
    }

    // ---------- Ứng dụng VueJS (build từ lab7-fe bằng Vite) ----------

    @Test
    void vueAppIsServed() throws Exception {
        // index.html phải có sẵn và trỏ tới bundle trong /assets
        String html = mvc.perform(get("/index.html"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(html).contains("<div id=\"app\">").contains("/assets/");

        // Mỗi file bundle mà index.html tham chiếu đều phải tải được
        Matcher m = Pattern.compile("/assets/[A-Za-z0-9._-]+").matcher(html);
        int found = 0;
        while (m.find()) {
            mvc.perform(get(m.group())).andExpect(status().isOk());
            found++;
        }
        assertThat(found).isGreaterThanOrEqualTo(2); // ít nhất 1 file .js và 1 file .css
    }

    @Test
    void missingRecordsReturnNotFound() throws Exception {
        mvc.perform(get("/categories/KHONG-CO")).andExpect(status().isNotFound());
        mvc.perform(put("/products/KHONG-CO").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(
                                new Product("KHONG-CO", "X", 1, LocalDate.now(), "C01"))))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/accounts/KHONG-CO")).andExpect(status().isNotFound());
    }
}
