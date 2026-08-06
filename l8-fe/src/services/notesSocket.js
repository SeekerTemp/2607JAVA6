import SockJS from 'sockjs-client'
import { Client } from '@stomp/stompjs'

const WS_URL = 'http://localhost:8080/ws'

// onNote  : gọi mỗi khi server phát một ghi chú mới
// onStatus: gọi với true/false mỗi khi kết nối lên hoặc rớt
export function connectNotesSocket(onNote, onStatus = () => {}) {
  const client = new Client({
    webSocketFactory: () => new SockJS(WS_URL),
    reconnectDelay: 5000,
    onConnect: () => {
      onStatus(true)
      client.subscribe('/topic/notes', (message) => {
        onNote(JSON.parse(message.body))
      })
    },
    onWebSocketClose: () => onStatus(false),
    onStompError: (frame) => {
      onStatus(false)
      console.error('Lỗi STOMP:', frame.headers['message'], frame.body)
    },
  })

  client.activate()
  return client
}
