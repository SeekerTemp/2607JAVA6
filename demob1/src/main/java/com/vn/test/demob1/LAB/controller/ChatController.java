package com.vn.test.demob1.LAB.controller;

import com.vn.test.demob1.LAB.model.ChatHistory;
import com.vn.test.demob1.LAB.model.ChatMessage;
import com.vn.test.demob1.LAB.service.ChatHistoryService;
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
 * LAB 8 - Bài 1 &amp; 2: gửi tin nhắn chung, tin nhắn riêng và quản lý danh sách người online.
 */
@Controller
public class ChatController {

    /** Đích của kênh chat chung. */
    public static final String PUBLIC_TOPIC = "/topic/public";

    /** Tiền tố đích của kênh chat riêng: /topic/private/{u1}--{u2}. */
    public static final String PRIVATE_TOPIC_PREFIX = "/topic/private/";

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatHistoryService chatHistoryService;

    /** sessionId -&gt; tên hiển thị (danh sách người đang online). */
    private final Map<String, String> onlineUsers = new ConcurrentHashMap<>();

    public ChatController(SimpMessagingTemplate messagingTemplate,
                          ChatHistoryService chatHistoryService) {
        this.messagingTemplate = messagingTemplate;
        this.chatHistoryService = chatHistoryService;
    }

    /**
     * Bài 1: gửi tin nhắn cho mọi người ở kênh chung.
     * Tin nhắn được ghi vào H2 để giữ đủ lịch sử, nhưng client không tải lại lịch sử này:
     * mỗi người chỉ đọc được tin nhắn phát sinh từ lúc họ vào phòng.
     */
    @MessageMapping("/chat")
    @SendTo(PUBLIC_TOPIC)
    public ChatMessage send(ChatMessage message) {
        message.setType(ChatMessage.MessageType.CHAT);
        return chatHistoryService.savePublic(message);
    }

    /**
     * Bài 2: người dùng tham gia -&gt; lưu vào SessionAttributes và trả về danh sách online (JOIN).
     */
    @MessageMapping("/username")
    @SendTo(PUBLIC_TOPIC)
    public ChatMessage addUser(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String name = message.getSenderName();
        Map<String, Object> attrs = headerAccessor.getSessionAttributes();
        if (attrs != null) {
            attrs.put("username", name);
        }
        onlineUsers.put(headerAccessor.getSessionId(), name);

        ChatMessage joined = new ChatMessage(ChatMessage.MessageType.JOIN,
                message.getSender(), name, name + " đã tham gia phòng chat");
        joined.setUsers(new ArrayList<>(onlineUsers.values()));
        return joined;
    }

    /**
     * Chat riêng giữa hai tài khoản: lưu lịch sử rồi đẩy tới đích riêng mà cả hai bên cùng subscribe.
     */
    @MessageMapping("/private")
    public void sendPrivate(ChatMessage message) {
        message.setType(ChatMessage.MessageType.PRIVATE);
        ChatMessage saved = chatHistoryService.savePrivate(message);
        String room = privateRoom(message.getSender(), message.getRecipient());
        messagingTemplate.convertAndSend(PRIVATE_TOPIC_PREFIX + room, saved);
    }

    /**
     * Bài 2: xử lý sự kiện ngắt kết nối -&gt; xóa user và phát danh sách online (LEAVE).
     */
    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        String name = onlineUsers.remove(event.getSessionId());
        if (name != null) {
            List<String> users = new ArrayList<>(onlineUsers.values());
            ChatMessage message = new ChatMessage(ChatMessage.MessageType.LEAVE,
                    null, name, name + " đã rời khỏi phòng chat");
            message.setUsers(users);
            messagingTemplate.convertAndSend(PUBLIC_TOPIC, message);
        }
    }

    /**
     * Tên phòng riêng không phụ thuộc thứ tự người gửi/người nhận
     * (dùng chung quy ước với bản ghi lịch sử để đích STOMP và kênh trong CSDL luôn khớp nhau).
     */
    public static String privateRoom(String a, String b) {
        return ChatHistory.privateRoom(a, b);
    }
}
