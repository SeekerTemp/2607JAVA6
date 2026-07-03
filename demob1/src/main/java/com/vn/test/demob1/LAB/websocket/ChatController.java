package com.vn.test.demob1.LAB.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LAB 8 - Bài 1 & 2: điều khiển gửi tin nhắn và quản lý danh sách người online.
 */
@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    // sessionId -> username (danh sách người đang online)
    private final Map<String, String> onlineUsers = new ConcurrentHashMap<>();

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Bài 1: gửi tin nhắn cho mọi người.
     */
    @MessageMapping("/chat")
    @SendTo("/topic/public")
    public ChatMessage send(ChatMessage message) {
        message.setType(ChatMessage.MessageType.CHAT);
        return message;
    }

    /**
     * Bài 2: người dùng tham gia -> lưu username và trả về danh sách online (JOIN).
     */
    @MessageMapping("/username")
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        Map<String, Object> attrs = headerAccessor.getSessionAttributes();
        if (attrs != null) {
            attrs.put("username", message.getSender());
        }
        onlineUsers.put(sessionId, message.getSender());

        return ChatMessage.builder()
                .type(ChatMessage.MessageType.JOIN)
                .sender(message.getSender())
                .content(message.getSender() + " đã tham gia phòng chat")
                .users(new ArrayList<>(onlineUsers.values()))
                .build();
    }

    /**
     * Bài 2: xử lý sự kiện ngắt kết nối -> xóa user và phát danh sách online (LEAVE).
     */
    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        String username = onlineUsers.remove(sessionId);
        if (username != null) {
            List<String> users = new ArrayList<>(onlineUsers.values());
            ChatMessage message = ChatMessage.builder()
                    .type(ChatMessage.MessageType.LEAVE)
                    .sender(username)
                    .content(username + " đã rời khỏi phòng chat")
                    .users(users)
                    .build();
            messagingTemplate.convertAndSend("/topic/public", message);
        }
    }
}
