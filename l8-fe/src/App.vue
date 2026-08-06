<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { getAllNotes, createNote } from './services/notesApi'
import { connectNotesSocket } from './services/notesSocket'

const author = ref('')
const content = ref('')
const notes = ref([])
const sending = ref(false)
const error = ref('')

const socketConnected = ref(false)

let socketClient = null

function formatTime(iso) {
  if (!iso) return ''
  return new Date(iso).toLocaleString()
}

async function loadNotes() {
  try {
    notes.value = await getAllNotes()
    error.value = ''
  } catch (e) {
    error.value = 'Không tải được danh sách ghi chú. Kiểm tra server đang chạy ở cổng 8080.'
    console.error(e)
  }
}

// chặn hiển thị trùng: cùng một id thì bỏ qua
function addNote(note) {
  if (notes.value.some((n) => n.id === note.id)) return
  notes.value.unshift(note)
}

async function sendNote() {
  if (sending.value) return
  if (!author.value.trim() || !content.value.trim()) return

  sending.value = true
  error.value = ''
  try {
    // req 2: gửi lên server qua POST /api/notes
    const created = await createNote(author.value.trim(), content.value.trim())
    // req 7: xoá nội dung ô nhập sau khi gửi
    content.value = ''

    // ghi chú vừa tạo sẽ quay lại qua WebSocket (req 4/5) nên bình thường
    // không tự thêm vào đây, để mọi client cùng nhận một nguồn dữ liệu.
    // Nhưng nếu socket đang rớt thì phải tự thêm, không thì gửi xong
    // màn hình của chính người gửi lại không thấy gì.
    if (!socketConnected.value) {
      addNote(created)
    }
  } catch (e) {
    error.value = 'Không gửi được ghi chú. Kiểm tra server đang chạy ở cổng 8080.'
    console.error(e)
  } finally {
    sending.value = false
  }
}

onMounted(async () => {
  await loadNotes()
  socketClient = connectNotesSocket(addNote, (connected) => {
    socketConnected.value = connected
    // nối lại sau khi rớt: tải lại danh sách cho khớp server
    if (connected && notes.value.length > 0) loadNotes()
  })
})

onUnmounted(() => {
  socketClient?.deactivate()
})
</script>

<template>
  <div class="notes-app">
    <h1>Ghi chú nhóm</h1>

    <form class="note-form" @submit.prevent="sendNote">
      <input
        v-model="author"
        type="text"
        placeholder="Tên người gửi"
        required
      />
      <input
        v-model="content"
        type="text"
        placeholder="Nội dung ghi chú"
        required
      />
      <!-- req 2: Enter trong input cũng submit form này, nên không cần bắt @keyup.enter -->
      <button type="submit" :disabled="sending">Gửi</button>
    </form>

    <p v-if="error" class="note-error">{{ error }}</p>
    <p v-else-if="!socketConnected" class="note-warn">
      Chưa kết nối realtime — ghi chú của người khác sẽ không tự hiện.
    </p>

    <ul class="note-list">
      <li v-for="note in notes" :key="note.id">
        <span class="note-author">{{ note.author }}</span>
        <span class="note-content">{{ note.content }}</span>
        <span class="note-time">{{ formatTime(note.createdAt) }}</span>
      </li>
      <li v-if="notes.length === 0" class="note-empty">Chưa có ghi chú nào.</li>
    </ul>
  </div>
</template>

<style scoped>
.notes-app {
  max-width: 480px;
  margin: 40px auto;
  font-family: sans-serif;
}

.note-form {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.note-form input {
  flex: 1;
  padding: 6px 8px;
}

.note-list {
  list-style: none;
  padding: 0;
}

.note-list li {
  display: flex;
  flex-direction: column;
  padding: 8px;
  border-bottom: 1px solid #ddd;
}

.note-author {
  font-weight: bold;
}

.note-time {
  font-size: 12px;
  color: #888;
}

.note-error {
  color: #c00;
  font-size: 13px;
}

.note-warn {
  color: #b26a00;
  font-size: 13px;
}

.note-empty {
  color: #888;
  font-size: 13px;
}
</style>
