<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

// Muc 9: moi API deu phai dang nhap -> gui Basic Auth (doi thanh MSSV / Lop)
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  auth: { username: 'PH12345', password: 'SOF3062' }
})

const list = ref([])

const load = async () => {
  const res = await api.get('/don-hang')
  list.value = res.data
}

onMounted(load)
</script>

<template>
  <h2>Danh sach Don hang</h2>
  <table border="1" cellpadding="6" cellspacing="0">
    <tr>
      <th>ID</th><th>Ma don hang</th><th>Ngay dat</th>
      <th>Tong tien</th><th>Ten khach hang</th><th>Dia chi</th>
    </tr>
    <tr v-for="dh in list" :key="dh.id">
      <td>{{ dh.id }}</td>
      <td>{{ dh.maDonHang }}</td>
      <td>{{ dh.ngayDat }}</td>
      <td>{{ dh.tongTien }}</td>
      <td>{{ dh.tenKhachHang }}</td>
      <td>{{ dh.diaChi }}</td>
    </tr>
  </table>
</template>
