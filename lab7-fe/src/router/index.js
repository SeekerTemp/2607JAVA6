import { createRouter, createWebHashHistory } from 'vue-router'
import CategoriesView from '../views/CategoriesView.vue'
import ProductsView from '../views/ProductsView.vue'
import UsersView from '../views/UsersView.vue'

// Dùng hash history (URL dạng /index.html#/products) để Spring Boot phục vụ
// file tĩnh là chạy được ngay, không cần thêm controller chuyển hướng.
const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/', redirect: '/categories' },
    { path: '/categories', name: 'categories', component: CategoriesView },
    { path: '/products', name: 'products', component: ProductsView },
    { path: '/users', name: 'users', component: UsersView },
  ],
})

export default router
