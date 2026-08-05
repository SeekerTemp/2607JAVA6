import SockJS from 'sockjs-client'
import { Client } from '@stomp/stompjs'

const WS_URL = 'http://localhost:8080/ws'

// onNote: callback invoked with the new note every time the server broadcasts one
export function connectNotesSocket(onNote) {
  const client = new Client({
    webSocketFactory: () => new SockJS(WS_URL),
    reconnectDelay: 5000,
    onConnect: () => {
      client.subscribe('/topic/notes', (message) => {
        onNote(JSON.parse(message.body))
      })
    },
  })

  client.activate()
  return client
}
