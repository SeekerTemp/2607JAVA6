package com.vn.test.demob1.dto;

/**
 * Body for POST /api/notes
 * { "author": "An", "content": "Nhớ chuẩn bị nội dung thuyết trình." }
 */
public record NoteCreateRequest(String author, String content) {
}
