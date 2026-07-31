<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <h1>LAB 6.1 - VueJS + Spring Boot REST API</h1>
  <nav>
    <RouterLink to="/categories">Loại hàng</RouterLink>
    <RouterLink to="/products">Sản phẩm</RouterLink>
    <RouterLink to="/users">Người sử dụng</RouterLink>
  </nav>
  <RouterView />
</template>
