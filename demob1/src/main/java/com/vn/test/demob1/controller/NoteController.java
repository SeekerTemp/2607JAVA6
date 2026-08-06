package com.vn.test.demob1.controller;

import com.vn.test.demob1.model.Note;
import com.vn.test.demob1.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // GET /api/notes
    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    // GET /api/notes/{id} - service ném 404 nếu không có
    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable Integer id) {
        return noteService.getNoteById(id);
    }

    // POST /api/notes - body { "author": "An", "content": "..." }
    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note request) {
        Note created = noteService.createNote(request.getAuthor(), request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/notes/{id} - body { "content": "..." }
    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Integer id, @RequestBody Note request) {
        return noteService.updateNote(id, request.getContent());
    }

    // DELETE /api/notes/{id} - 200 khi xóa xong
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Integer id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok().build();
    }
}
