import { createRouter, createWebHistory } from 'vue-router';
import CartPage from '../pages/CartPage.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'cart',
      component: CartPage
    }
  ]
});

export default router;
