package com.vn.test.demob1.LAB.service;

import com.vn.test.demob1.LAB.model.ChatMessage;

import java.util.List;

/**
 * LAB 8: lưu và đọc lại lịch sử tin nhắn.
 */
public interface ChatHistoryService {

    /**
     * Lưu tin nhắn của kênh chung. Trả về tin nhắn đã được gán thời điểm gửi.
     */
    ChatMessage savePublic(ChatMessage message);

    /**
     * Lưu tin nhắn riêng giữa người gửi và người nhận.
     */
    ChatMessage savePrivate(ChatMessage message);

    /**
     * Toàn bộ lịch sử chat riêng giữa hai tài khoản, từ tin nhắn đầu tiên.
     */
    List<ChatMessage> findPrivateHistory(String user1, String user2);
}
