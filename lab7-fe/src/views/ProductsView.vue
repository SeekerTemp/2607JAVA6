<script setup>
// LAB 7 - Bài 2: quản lý sản phẩm {id, name, price, date, categoryId}
// categoryId là khóa ngoại tham chiếu đến loại hàng ở Bài 1
import { ref, onMounted } from 'vue'
import { api, apiError } from '../api.js'

const list = ref([])
const categories = ref([])   // Danh sách loại hàng cho dropdown
const emptyForm = () => ({ id: '', name: '', price: 0, date: '', categoryId: '' })
const form = ref(emptyForm())
const editing = ref(false)
const message = ref('')

// Hiển thị tên loại hàng thay vì mã trong bảng
const categoryName = (id) => {
  const c = categories.value.find(c => c.id === id)
  return c ? c.name : id
}

// Ngày rỗng phải gửi null để Jackson không lỗi khi đọc LocalDate
const payload = () => ({
  ...form.value,
  price: Number(form.value.price) || 0,
  date: form.value.date ? form.value.date : null,
})

const load = async () => {
  try {
    const resp = await api.get('/products')
    list.value = resp.data
  } catch (err) {
    message.value = apiError(err, 'Lỗi khi tải danh sách sản phẩm!')
  }
}

const loadCategories = async () => {
  try {
    const resp = await api.get('/categories')
    categories.value = resp.data
  } catch (err) {
    message.value = apiError(err, 'Lỗi khi tải danh sách loại hàng!')
  }
}

const reset = () => {
  form.value = emptyForm()
  editing.value = false
  message.value = ''
}

const edit = (p) => {
  form.value = Object.assign({}, p, { date: p.date || '' })
  editing.value = true
  message.value = ''
}

const create = async () => {
  message.value = ''
  try {
    await api.post('/products', payload())
    await load()
    reset()
  } catch (err) {
    message.value = apiError(err, 'Lỗi khi thêm sản phẩm!')
  }
}

const update = async () => {
  message.value = ''
  try {
    await api.put(`/products/${form.value.id}`, payload())
    await load()
  } catch (err) {
    message.value = apiError(err, 'Lỗi khi cập nhật sản phẩm!')
  }
}

const remove = async (id) => {
  if (!confirm(`Xóa sản phẩm ${id}?`)) return
  message.value = ''
  try {
    await api.delete(`/products/${id}`)
    await load()
    reset()
  } catch (err) {
    message.value = apiError(err, 'Lỗi khi xóa sản phẩm!')
  }
}

onMounted(async () => {
  await loadCategories()
  await load()
})
</script>

<template>
  <h3>QUẢN LÝ SẢN PHẨM</h3>

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
    <label>Price</label>
    <input type="number" v-model.number="form.price">
  </div>
  <div class="form-row">
    <label>Date</label>
    <input type="date" v-model="form.date">
  </div>
  <div class="form-row">
    <label>Category</label>
    <select v-model="form.categoryId">
      <option value="">-- Chọn loại hàng --</option>
      <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
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
        <th>Id</th>
        <th>Name</th>
        <th>Price</th>
        <th>Date</th>
        <th>Category</th>
        <th>Actions</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="p in list" :key="p.id">
        <td>{{ p.id }}</td>
        <td>{{ p.name }}</td>
        <td>{{ p.price }}</td>
        <td>{{ p.date }}</td>
        <td>{{ categoryName(p.categoryId) }}</td>
        <td>
          <a @click="edit(p)">Edit</a>
          <a @click="remove(p.id)">Delete</a>
        </td>
      </tr>
      <tr v-if="list.length === 0">
        <td colspan="6">Chưa có sản phẩm nào.</td>
      </tr>
    </tbody>
  </table>
</template>
