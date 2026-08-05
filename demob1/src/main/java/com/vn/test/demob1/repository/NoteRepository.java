package com.vn.test.demob1.repository;

import com.vn.test.demob1.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    // req 1 (đề phụ): danh sách ghi chú, sắp xếp theo thời gian tạo giảm dần
    List<Note> findAllByOrderByCreatedAtDesc();
}
