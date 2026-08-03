package com.vn.test.demob1.LAB.service;

import com.vn.test.demob1.LAB.model.ChatHistory;
import com.vn.test.demob1.LAB.model.ChatMessage;
import com.vn.test.demob1.LAB.repository.ChatHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatHistoryServiceImpl implements ChatHistoryService {

    private final ChatHistoryRepository chatHistoryRepository;

    public ChatHistoryServiceImpl(ChatHistoryRepository chatHistoryRepository) {
        this.chatHistoryRepository = chatHistoryRepository;
    }

    @Override
    @Transactional
    public ChatMessage savePublic(ChatMessage message) {
        return save(ChatHistory.PUBLIC_ROOM, message);
    }

    @Override
    @Transactional
    public ChatMessage savePrivate(ChatMessage message) {
        String room = ChatHistory.privateRoom(message.getSender(), message.getRecipient());
        return save(room, message);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessage> findPrivateHistory(String user1, String user2) {
        String room = ChatHistory.privateRoom(user1, user2);
        List<ChatMessage> messages = new ArrayList<>();
        for (ChatHistory row : chatHistoryRepository.findByRoomOrderBySentAtAscIdAsc(room)) {
            messages.add(row.toMessage());
        }
        return messages;
    }

    /**
     * Gán thời điểm gửi rồi ghi xuống H2; tin nhắn trả về đã có sentAt để client hiển thị.
     */
    private ChatMessage save(String room, ChatMessage message) {
        LocalDateTime sentAt = LocalDateTime.now();
        message.setSentAt(sentAt);
        chatHistoryRepository.save(new ChatHistory(room, message, sentAt));
        return message;
    }
}
