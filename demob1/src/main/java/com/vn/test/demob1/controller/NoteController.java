package com.vn.test.demob1.controller;

import com.vn.test.demob1.dto.NoteCreateRequest;
import com.vn.test.demob1.dto.NoteUpdateRequest;
import com.vn.test.demob1.entity.Note;
import com.vn.test.demob1.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // GET /api/notes/{id} -> 404 handled inside service via ResponseStatusException
    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable Long id) {
        return noteService.getNoteById(id);
    }

    // POST /api/notes -> also broadcasts over WebSocket
    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody NoteCreateRequest request) {
        Note created = noteService.createNote(request.author(), request.content());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/notes/{id}
    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Long id, @RequestBody NoteUpdateRequest request) {
        return noteService.updateNote(id, request.content());
    }

    // DELETE /api/notes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok().build();
    }
}
