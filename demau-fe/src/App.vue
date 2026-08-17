<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

// Muc 9: moi API deu phai dang nhap -> gui Basic Auth (doi thanh MSSV / Lop)
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  auth: { username: 'PH12345', password: 'SOF3062' }
})

const list = ref([])
const form = ref({ id: null, khachHangId: 1, maDonHang: '', ngayDat: '', tongTien: 0 })
const loi = ref({})   // map field -> message do ApiExceptionHandler tra ve (muc 8)

const load = async () => {
  list.value = (await api.get('/don-hang')).data
}

const body = () => ({
  khachHang: { id: form.value.khachHangId },
  maDonHang: form.value.maDonHang,
  ngayDat: form.value.ngayDat || null,
  tongTien: form.value.tongTien
})

// goi API roi lam moi bang; neu 400 thi hien loi tung field (muc 8)
const goi = async (req) => {
  try {
    await req
    huy()
    load()
  } catch (e) {
    loi.value = e.response.data
  }
}

// nut Them - them ban ghi moi (muc 4)
const them = () => goi(api.post('/don-hang', body()))

// nut Luu - luu ban ghi dang sua (muc 5)
const luu = () => goi(api.put('/don-hang/' + form.value.id, body()))

const sua = (dh) => {
  form.value = { id: dh.id, khachHangId: 1, maDonHang: dh.maDonHang, ngayDat: dh.ngayDat, tongTien: dh.tongTien }
  loi.value = {}
}

// xoa (muc 6)
const xoa = async (id) => {
  await api.delete('/don-hang/' + id)
  load()
}

const huy = () => {
  form.value = { id: null, khachHangId: 1, maDonHang: '', ngayDat: '', tongTien: 0 }
  loi.value = {}
}

onMounted(load)
</script>

<template>
  <h2>{{ form.id ? 'Sua don hang #' + form.id : 'Them don hang' }}</h2>
  <p>
    Khach hang ID: <input v-model="form.khachHangId" size="3">
    <span style="color:red">{{ loi.khachHang }}</span>
  </p>
  <p>
    Ma don hang: <input v-model="form.maDonHang">
    <span style="color:red">{{ loi.maDonHang }}</span>
  </p>
  <p>
    Ngay dat: <input v-model="form.ngayDat" type="date">
    <span style="color:red">{{ loi.ngayDat }}</span>
  </p>
  <p>
    Tong tien: <input v-model="form.tongTien" type="number">
    <span style="color:red">{{ loi.tongTien }}</span>
  </p>
  <p>
    <button v-if="!form.id" @click="them">Them</button>
    <button v-if="form.id" @click="luu">Luu</button>
    <button @click="huy">Huy</button>
  </p>

  <h2>Danh sach Don hang</h2>
  <table border="1" cellpadding="6" cellspacing="0">
    <tr>
      <th>ID</th><th>Ma don hang</th><th>Ngay dat</th>
      <th>Tong tien</th><th>Ten khach hang</th><th>Dia chi</th><th>Thao tac</th>
    </tr>
    <tr v-for="dh in list" :key="dh.id">
      <td>{{ dh.id }}</td>
      <td>{{ dh.maDonHang }}</td>
      <td>{{ dh.ngayDat }}</td>
      <td>{{ dh.tongTien }}</td>
      <td>{{ dh.tenKhachHang }}</td>
      <td>{{ dh.diaChi }}</td>
      <td>
        <button @click="sua(dh)">Sua</button>
        <button @click="xoa(dh.id)">Xoa</button>
      </td>
    </tr>
  </table>
</template>
