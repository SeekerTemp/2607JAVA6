package com.vn.test.demob1;

import com.vn.test.demob1.model.Note;
import com.vn.test.demob1.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Kiem tra 5 API cua de phu.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class NoteApiTests {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private NoteRepository noteRepository;

    @BeforeEach
    void seed() {
        noteRepository.deleteAll();
        // ba ghi chu mau cua de bai, tao cach nhau de kiem tra thu tu sap xep
        noteRepository.save(withCreatedAt(new Note("Trung", "Ghi chu hop nhom vao thu Hai."), 30));
        noteRepository.save(withCreatedAt(new Note("Lan", "Nho nop bai truoc thu Sau."), 20));
        noteRepository.save(withCreatedAt(new Note("Bao", "Lien he giang vien ve deadline."), 10));
    }

    private Note withCreatedAt(Note note, int phutTruoc) {
        note.setCreatedAt(java.time.LocalDateTime.now().minusMinutes(phutTruoc));
        return note;
    }

    // De phu #1: GET /api/notes - sap xep theo created_at giam dan
    @Test
    void layTatCaGhiChuSapXepGiamDan() {
        ResponseEntity<Note[]> res = rest.getForEntity("/api/notes", Note[].class);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        Note[] notes = res.getBody();
        assertNotNull(notes);
        assertEquals(3, notes.length);
        assertEquals("Bao", notes[0].getAuthor());
        assertEquals("Trung", notes[2].getAuthor());
        for (int i = 1; i < notes.length; i++) {
            assertTrue(notes[i - 1].getCreatedAt().isAfter(notes[i].getCreatedAt()),
                    "Danh sach chua sap xep giam dan theo created_at");
        }
    }

    // De phu #2: GET /api/notes/{id}
    @Test
    void layGhiChuTheoId() {
        Integer id = noteRepository.findAllByOrderByCreatedAtDesc().get(0).getId();

        ResponseEntity<Note> res = rest.getForEntity("/api/notes/" + id, Note.class);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
        assertEquals("Bao", res.getBody().getAuthor());
    }

    // De phu #2: khong tim thay -> 404
    @Test
    void layGhiChuKhongTonTaiTra404() {
        ResponseEntity<String> res = rest.getForEntity("/api/notes/999999", String.class);

        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    // De phu #3: POST /api/notes - tra ve ban ghi co id va created_at
    @Test
    void taoGhiChuMoi() {
        ResponseEntity<Note> res = rest.postForEntity("/api/notes",
                json(Map.of("author", "An", "content", "Nho chuan bi noi dung thuyet trinh.")),
                Note.class);

        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        Note created = res.getBody();
        assertNotNull(created);
        assertNotNull(created.getId(), "Ghi chu moi phai co id");
        assertNotNull(created.getCreatedAt(), "Ghi chu moi phai co created_at");
        assertEquals("An", created.getAuthor());
        assertEquals(4, noteRepository.count());
    }

    // Validate: content rong -> 400
    @Test
    void taoGhiChuThieuNoiDungTra400() {
        ResponseEntity<String> res = rest.postForEntity("/api/notes",
                json(Map.of("author", "An", "content", "   ")), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    // De phu #4: PUT /api/notes/{id} - doi noi dung va set updated_at
    @Test
    void capNhatNoiDungGhiChu() {
        Note goc = noteRepository.findAllByOrderByCreatedAtDesc().get(0);

        ResponseEntity<Note> res = rest.exchange("/api/notes/" + goc.getId(), HttpMethod.PUT,
                json(Map.of("content", "Da chuan bi xong bai thuyet trinh.")), Note.class);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        Note updated = res.getBody();
        assertNotNull(updated);
        assertEquals("Da chuan bi xong bai thuyet trinh.", updated.getContent());
        assertNotNull(updated.getUpdatedAt(), "PUT phai set updated_at");
        assertEquals(goc.getAuthor(), updated.getAuthor(), "PUT khong duoc doi author");
    }

    // De phu #5: DELETE /api/notes/{id} - tra 200
    @Test
    void xoaGhiChu() {
        Integer id = noteRepository.findAllByOrderByCreatedAtDesc().get(0).getId();

        ResponseEntity<Void> res = rest.exchange("/api/notes/" + id, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(2, noteRepository.count());
        assertEquals(HttpStatus.NOT_FOUND,
                rest.getForEntity("/api/notes/" + id, String.class).getStatusCode());
    }

    // CORS mo cho moi origin: preflight tu mot origin bat ky phai duoc chap nhan
    @Test
    void chapNhanCorsTuMoiOrigin() {
        for (String origin : new String[]{"http://localhost:5173", "http://example.com", "http://192.168.1.50:3000"}) {
            HttpHeaders headers = new HttpHeaders();
            headers.setOrigin(origin);
            headers.setAccessControlRequestMethod(HttpMethod.POST);

            ResponseEntity<Void> res = rest.exchange("/api/notes", HttpMethod.OPTIONS,
                    new HttpEntity<>(headers), Void.class);

            assertEquals(HttpStatus.OK, res.getStatusCode(), "Preflight bi tu choi voi origin " + origin);
            assertEquals(origin, res.getHeaders().getFirst("Access-Control-Allow-Origin"),
                    "Thieu Access-Control-Allow-Origin cho " + origin);
        }
    }

    private HttpEntity<Map<String, String>> json(Map<String, String> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}
