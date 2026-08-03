package com.vn.test.demob1.LAB.repository;

import com.vn.test.demob1.LAB.model.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LAB 8: truy vấn lịch sử tin nhắn đã lưu trong H2.
 */
@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {

    /**
     * Lấy toàn bộ tin nhắn của một kênh theo đúng thứ tự thời gian gửi.
     */
    List<ChatHistory> findByRoomOrderBySentAtAscIdAsc(String room);
}
