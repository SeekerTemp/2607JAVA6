package com.vn.test.demob1.LAB.controller;

import com.vn.test.demob1.LAB.model.ChatMessage;
import com.vn.test.demob1.LAB.service.ChatHistoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * LAB 8: API tải lịch sử tin nhắn.
 * Chỉ mở cho chat riêng - kênh chung cố ý không cho tải lại lịch sử,
 * người dùng chỉ thấy tin nhắn mới kể từ khi vào phòng.
 */
@RestController
@RequestMapping("/api/history")
public class ChatHistoryController {

    private final ChatHistoryService chatHistoryService;

    public ChatHistoryController(ChatHistoryService chatHistoryService) {
        this.chatHistoryService = chatHistoryService;
    }

    /**
     * Toàn bộ tin nhắn riêng giữa hai tài khoản, tính từ tin nhắn đầu tiên.
     */
    @GetMapping("/private")
    public List<ChatMessage> privateHistory(@RequestParam String user1,
                                            @RequestParam String user2) {
        return chatHistoryService.findPrivateHistory(user1, user2);
    }
}
