package com.vn.test.demob1;

import com.vn.test.demob1.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Đề chính #4: ghi chú mới phải được phát qua WebSocket tới mọi client đang kết nối.
 * Test này nối vào /ws bằng SockJS + STOMP, subscribe /topic/notes rồi tạo ghi chú.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NoteWebSocketBroadcastTests {

    @LocalServerPort
    private int port;

    @Autowired
    private NoteService noteService;

    @Test
    void ghiChuMoiDuocPhatQuaWebSocket() throws Exception {
        WebSocketStompClient stompClient = new WebSocketStompClient(
                new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient()))));

        StompSession session = stompClient
                .connectAsync("ws://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {
                })
                .get(5, TimeUnit.SECONDS);

        BlockingQueue<String> received = new LinkedBlockingQueue<>();
        session.subscribe("/topic/notes", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                received.add(new String((byte[]) payload, StandardCharsets.UTF_8));
            }
        });

        // chờ broker ghi nhận subscription trước khi phát
        Thread.sleep(500);

        noteService.createNote("An", "Nho chuan bi noi dung thuyet trinh.");

        String payload = received.poll(5, TimeUnit.SECONDS);
        assertNotNull(payload, "Khong nhan duoc ghi chu nao tu /topic/notes");
        assertTrue(payload.contains("\"author\":\"An\""), "Payload thieu author: " + payload);
        assertTrue(payload.contains("Nho chuan bi noi dung thuyet trinh."), "Payload thieu content: " + payload);
        assertTrue(payload.contains("\"id\""), "Payload thieu id: " + payload);
        assertTrue(payload.contains("\"createdAt\""), "Payload thieu createdAt: " + payload);

        session.disconnect();
        stompClient.stop();
    }
}
