<script setup>
// LAB 7 - Bài 1: quản lý loại hàng {id, name}
import { ref, onMounted } from 'vue'
import { api, apiError } from '../api.js'

const list = ref([])                       // Danh sách loại hàng
const form = ref({ id: '', name: '' })     // Loại hàng đang thao tác
const editing = ref(false)                 // Cờ đánh dấu đang sửa
const message = ref('')                    // Thông báo lỗi cho người dùng

const load = async () => {
  try {
    const resp = await api.get('/categories')
    list.value = resp.data
  } catch (err) {
    message.value = apiError(err, 'Lỗi khi tải danh sách loại hàng!')
  }
}

const reset = () => {
  form.value = { id: '', name: '' }
  editing.value = false
  message.value = ''
}

// Đưa dữ liệu dòng vào form để sửa
const edit = (c) => {
  form.value = Object.assign({}, c)
  editing.value = true
  message.value = ''
}

const create = async () => {
  message.value = ''
  try {
    await api.post('/categories', form.value)
    await load()
    reset()
  } catch (err) {
    message.value = apiError(err, 'Lỗi khi thêm loại hàng!')
  }
}

const update = async () => {
  message.value = ''
  try {
    await api.put(`/categories/${form.value.id}`, form.value)
    await load()
  } catch (err) {
    message.value = apiError(err, 'Lỗi khi cập nhật loại hàng!')
  }
}

const remove = async (id) => {
  if (!confirm(`Xóa loại hàng ${id}?`)) return
  message.value = ''
  try {
    await api.delete(`/categories/${id}`)
    await load()
    reset()
  } catch (err) {
    message.value = apiError(err, 'Lỗi khi xóa loại hàng!')
  }
}

onMounted(load)
</script>

<template>
  <h3>QUẢN LÝ LOẠI HÀNG</h3>

  <div class="msg" v-if="message">{{ message }}</div>

  <div class="form-row">
    <label>Id</label>
    <!-- Khóa chính: khóa lại khi đang sửa để không tạo nhầm dòng mới -->
    <input type="text" v-model="form.id" :readonly="editing">
  </div>
  <div class="form-row">
    <label>Name</label>
    <input type="text" v-model="form.name">
  </div>
  <div class="form-row">
    <button @click="create" :disabled="editing">Create</button>
    <button @click="update" :disabled="!editing">Update</button>
    <button @click="reset">Reset</button>
  </div>

  <table>
    <thead>
      <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Actions</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="c in list" :key="c.id">
        <td>{{ c.id }}</td>
        <td>{{ c.name }}</td>
        <td>
          <a @click="edit(c)">Edit</a>
          <a @click="remove(c.id)">Delete</a>
        </td>
      </tr>
      <tr v-if="list.length === 0">
        <td colspan="3">Chưa có loại hàng nào.</td>
      </tr>
    </tbody>
  </table>
</template>
