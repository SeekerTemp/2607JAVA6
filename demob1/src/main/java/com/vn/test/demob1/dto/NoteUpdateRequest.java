package com.vn.test.demob1.dto;

/**
 * Body for PUT /api/notes/{id}
 * { "content": "Đã chuẩn bị xong bài thuyết trình." }
 */
public record NoteUpdateRequest(String content) {
}
