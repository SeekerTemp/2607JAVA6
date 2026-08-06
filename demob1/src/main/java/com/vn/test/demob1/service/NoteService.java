package com.vn.test.demob1.service;

import com.vn.test.demob1.model.Note;

import java.util.List;

public interface NoteService {

    // đề phụ #1
    List<Note> getAllNotes();

    // đề phụ #2
    Note getNoteById(Integer id);

    // đề chính #3, #4 + đề phụ #3
    Note createNote(String author, String content);

    // đề phụ #4
    Note updateNote(Integer id, String content);

    // đề phụ #5
    void deleteNote(Integer id);
}
