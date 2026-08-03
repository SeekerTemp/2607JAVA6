package com.vn.test.demob1.LAB.model;

import java.util.List;

/**
 * LAB 8: cấu trúc dữ liệu tin nhắn chat.
 *  - type: CHAT (tin nhắn chung), PRIVATE (tin nhắn riêng), JOIN (tham gia), LEAVE (rời đi)
 *  - sender: username người gửi
 *  - senderName: tên hiển thị của người gửi
 *  - recipient: username người nhận (chỉ dùng cho tin nhắn riêng)
 *  - content: nội dung tin nhắn
 *  - users: danh sách người đang online (cho JOIN/LEAVE)
 */
public class ChatMessage {

    public enum MessageType {
        CHAT, PRIVATE, JOIN, LEAVE
    }

    private MessageType type;
    private String sender;
    private String senderName;
    private String recipient;
    private String content;
    private List<String> users;

    public ChatMessage() {
    }

    public ChatMessage(MessageType type, String sender, String senderName, String content) {
        this.type = type;
        this.sender = sender;
        this.senderName = senderName;
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
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

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
