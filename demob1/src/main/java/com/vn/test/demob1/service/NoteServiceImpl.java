package com.vn.test.demob1.service;

import com.vn.test.demob1.model.Note;
import com.vn.test.demob1.repository.NoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    // kênh WebSocket mà mọi client đang subscribe
    private static final String NOTES_TOPIC = "/topic/notes";

    private final NoteRepository noteRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NoteServiceImpl(NoteRepository noteRepository, SimpMessagingTemplate messagingTemplate) {
        this.noteRepository = noteRepository;
        this.messagingTemplate = messagingTemplate;
    }

    // GET /api/notes - mới nhất lên trước (đề chính #6, đề phụ #1)
    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAllByOrderByCreatedAtDesc();
    }

    // GET /api/notes/{id} - không thấy thì 404 (đề phụ #2)
    @Override
    public Note getNoteById(Integer id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Khong tim thay ghi chu id = " + id));
    }

    // POST /api/notes - lưu vào DB rồi phát cho mọi client (đề chính #3, #4)
    @Override
    public Note createNote(String author, String content) {
        requireText(author, "author");
        requireText(content, "content");

        Note saved = noteRepository.save(new Note(author.trim(), content.trim()));

        // đề chính #4: broadcast ghi chú mới qua WebSocket
        messagingTemplate.convertAndSend(NOTES_TOPIC, saved);
        return saved;
    }

    // PUT /api/notes/{id} - đổi nội dung, set updated_at (đề phụ #4)
    @Override
    public Note updateNote(Integer id, String content) {
        requireText(content, "content");

        Note note = getNoteById(id);
        note.setContent(content.trim());
        note.setUpdatedAt(LocalDateTime.now());
        return noteRepository.save(note);
    }

    // DELETE /api/notes/{id} (đề phụ #5)
    @Override
    public void deleteNote(Integer id) {
        noteRepository.delete(getNoteById(id));
    }

    private void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Truong '" + field + "' khong duoc de trong");
        }
    }
}
