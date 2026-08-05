<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { getAllNotes, createNote } from './services/notesApi'
import { connectNotesSocket } from './services/notesSocket'

const author = ref('')
const content = ref('')
const notes = ref([])

let socketClient = null

function formatTime(iso) {
  if (!iso) return ''
  return new Date(iso).toLocaleString()
}

async function loadNotes() {
  notes.value = await getAllNotes()
}

async function sendNote() {
  if (!author.value.trim() || !content.value.trim()) return

  await createNote(author.value.trim(), content.value.trim())
  // req 7: xoá nội dung ô nhập sau khi gửi
  content.value = ''
  // the new note itself arrives back through the WebSocket broadcast (req 4/5),
  // so we don't push it into `notes` here — that keeps every connected client in sync.
}

onMounted(async () => {
  await loadNotes()
  socketClient = connectNotesSocket((newNote) => {
    notes.value.unshift(newNote)
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
        @keyup.enter="sendNote"
      />
      <button type="submit">Gửi</button>
    </form>

    <ul class="note-list">
      <li v-for="note in notes" :key="note.id">
        <span class="note-author">{{ note.author }}</span>
        <span class="note-content">{{ note.content }}</span>
        <span class="note-time">{{ formatTime(note.createdAt) }}</span>
      </li>
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
</style>
