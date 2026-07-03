package com.vn.test.demob1.LAB.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * LAB 8: cấu trúc dữ liệu tin nhắn chat.
 *  - type: CHAT (tin nhắn), JOIN (tham gia), LEAVE (rời đi)
 *  - sender: người gửi
 *  - content: nội dung tin nhắn
 *  - users: danh sách người đang online (cho JOIN/LEAVE)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    private MessageType type;
    private String sender;
    private String content;
    private List<String> users;
}
