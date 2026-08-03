package com.vn.test.demob1.LAB.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * LAB 8: bản ghi lịch sử tin nhắn lưu xuống H2 (in-memory -> tắt app là mất).
 *  - room: "public" cho kênh chung, hoặc "u2--u3" cho kênh riêng giữa hai người
 *  - sentAt: thời điểm gửi, dùng để sắp xếp khi tải lại lịch sử
 */
@Entity
@Table(name = "chat_history")
public class ChatHistory {

    /** Tên kênh chung. */
    public static final String PUBLIC_ROOM = "public";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Kênh chứa tin nhắn: "public" hoặc tên phòng riêng. */
    @Column(nullable = false)
    private String room;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatMessage.MessageType type;

    private String sender;

    private String senderName;

    /** Chỉ có giá trị với tin nhắn riêng. */
    private String recipient;

    @Column(length = 1000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    public ChatHistory() {
    }

    public ChatHistory(String room, ChatMessage message, LocalDateTime sentAt) {
        this.room = room;
        this.type = message.getType();
        this.sender = message.getSender();
        this.senderName = message.getSenderName();
        this.recipient = message.getRecipient();
        this.content = message.getContent();
        this.sentAt = sentAt;
    }

    /**
     * Tên phòng riêng của hai tài khoản, không phụ thuộc thứ tự người gửi/người nhận,
     * nhờ vậy cả hai bên cùng đọc/ghi trên một kênh duy nhất.
     */
    public static String privateRoom(String a, String b) {
        return a.compareTo(b) <= 0 ? a + "--" + b : b + "--" + a;
    }

    /**
     * Chuyển bản ghi trong CSDL về đúng dạng tin nhắn mà client đang dùng.
     */
    public ChatMessage toMessage() {
        ChatMessage message = new ChatMessage(type, sender, senderName, content);
        message.setRecipient(recipient);
        message.setSentAt(sentAt);
        return message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public ChatMessage.MessageType getType() {
        return type;
    }

    public void setType(ChatMessage.MessageType type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}
