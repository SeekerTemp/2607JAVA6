<script setup>
// LAB 7 - Bài 3: quản lý người sử dụng {username, password, fullname, enabled, role}
// role thuộc {"USER", "ADMIN"}
import { ref, onMounted } from 'vue'
import { api, apiError } from '../api.js'

const list = ref([])
const emptyForm = () => ({ username: '', password: '', fullname: '', enabled: false, role: 'USER' })
const form = ref(emptyForm())
const editing = ref(false)
const message = ref('')

const load = async () => {
  message.value = ''
  try {
    const resp = await api.get('/accounts')
    list.value = Array.isArray(resp.data) ? resp.data : []
  } catch (err) {
    list.value = []
    message.value = apiError(err, 'Lỗi khi tải danh sách người dùng!')
  }
}

const reset = () => {
  form.value = emptyForm()
  editing.value = false
  message.value = ''
}

const edit = (u) => {
  form.value = Object.assign({}, u)
  editing.value = true
  message.value = ''
}

const create = async () => {
  message.value = ''
  try {
    await api.post('/accounts', form.value)
    await load()
    reset()
  } catch (err) {
    message.value = apiError(err, 'Lỗi khi thêm người dùng!')
  }
}

const update = async () => {
  message.value = ''
  try {
    await api.put(`/accounts/${form.value.username}`, form.value)
    await load()
  } catch (err) {
    message.value = apiError(err, 'Lỗi khi cập nhật người dùng!')
  }
}

const remove = async (username) => {
  if (!confirm(`Xóa người dùng ${username}?`)) return
  message.value = ''
  try {
    await api.delete(`/accounts/${username}`)
    await load()
    reset()
  } catch (err) {
    message.value = apiError(err, 'Lỗi khi xóa người dùng!')
  }
}

onMounted(load)
</script>

<template>
  <h3>QUẢN LÝ NGƯỜI SỬ DỤNG</h3>

  <div class="msg" v-if="message" style="color: red; margin-bottom: 10px;">{{ message }}</div>

  <div class="form-row">
    <label>Username</label>
    <!-- Khóa chính: khóa lại khi đang sửa để không tạo nhầm dòng mới -->
    <input type="text" v-model="form.username" :readonly="editing">
  </div>
  <div class="form-row">
    <label>Password</label>
    <input type="text" v-model="form.password">
  </div>
  <div class="form-row">
    <label>Fullname</label>
    <input type="text" v-model="form.fullname">
  </div>
  <div class="form-row">
    <label>Enabled</label>
    <input type="checkbox" v-model="form.enabled">
  </div>
  <div class="form-row">
    <label>Role</label>
    <select v-model="form.role">
      <option value="USER">USER</option>
      <option value="ADMIN">ADMIN</option>
    </select>
  </div>
  <div class="form-row">
    <button @click="create" :disabled="editing">Create</button>
    <button @click="update" :disabled="!editing">Update</button>
    <button @click="reset">Reset</button>
  </div>

  <table>
    <thead>
      <tr>
        <th>Username</th>
        <th>Fullname</th>
        <th>Enabled</th>
        <th>Role</th>
        <th>Actions</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="u in list" :key="u.username">
        <td>{{ u.username }}</td>
        <td>{{ u.fullname }}</td>
        <td>{{ u.enabled ? 'Có' : 'Không' }}</td>
        <td>{{ u.role }}</td>
        <td>
          <a @click="edit(u)">Edit</a>
          <a @click="remove(u.username)">Delete</a>
        </td>
      </tr>
      <tr v-if="list.length === 0">
        <td colspan="5">Chưa có người dùng nào.</td>
      </tr>
    </tbody>
  </table>
</template>
