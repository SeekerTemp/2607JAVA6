package com.vn.test.demob1.service;

import com.vn.test.demob1.entity.Note;
import com.vn.test.demob1.repository.NoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {

    private static final String NOTES_TOPIC = "/topic/notes";

    private final NoteRepository noteRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NoteService(NoteRepository noteRepository, SimpMessagingTemplate messagingTemplate) {
        this.noteRepository = noteRepository;
        this.messagingTemplate = messagingTemplate;
    }

    // GET /api/notes (đề phụ #1) — mới nhất trước
    public List<Note> getAllNotes() {
        return noteRepository.findAllByOrderByCreatedAtDesc();
    }

    // GET /api/notes/{id} (đề phụ #2)
    public Note getNoteById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note " + id + " not found"));
    }

    // POST /api/notes (đề chính #2,#3 + đề phụ #3) — lưu rồi phát realtime qua WebSocket (đề chính #4)
    public Note createNote(String author, String content) {
        Note note = new Note();
        note.setAuthor(author);
        note.setContent(content);

        Note saved = noteRepository.save(note);
        messagingTemplate.convertAndSend(NOTES_TOPIC, saved);
        return saved;
    }

    // PUT /api/notes/{id} (đề phụ #4)
    public Note updateNote(Long id, String content) {
        Note note = getNoteById(id);
        note.setContent(content);
        note.setUpdatedAt(LocalDateTime.now());
        return noteRepository.save(note);
    }

    // DELETE /api/notes/{id} (đề phụ #5)
    public void deleteNote(Long id) {
        Note note = getNoteById(id);
        noteRepository.delete(note);
    }
}
