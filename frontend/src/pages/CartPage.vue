<template>
  <MainLayout>
    <section class="page-grid">
      <CartHeader
        :user-id="userId"
        :cart-id="cartId"
        :disabled="isBusy"
        @update:user-id="handleUserIdChange"
        @load-cart="createOrLoadCart"
      />

      <div v-if="errorMessage" class="feedback feedback--error">
        {{ errorMessage }}
      </div>

      <div v-if="successMessage" class="feedback feedback--success">
        {{ successMessage }}
      </div>

      <div class="workspace-grid">
        <div class="workspace-grid__main">
          <AddProductForm :disabled="isBusy || !hasCart" @add-product="handleAddProduct" />
          <CartItemsTable
            :items="items"
            :disabled="isBusy || !hasCart"
            @update-quantity="handleUpdateQuantity"
            @remove-item="removeItem"
          />
        </div>

        <CartSummary
          :items="items"
          :total="total"
          :cart="cart"
          :disabled="isBusy || !hasCart"
          @remove-item="removeItem"
          @refresh="fetchCart"
        />
      </div>

      <div v-if="isLoading" class="loading-badge">
        Consultando carrito...
      </div>
    </section>
  </MainLayout>
</template>

<script setup>
import { computed } from 'vue';
import MainLayout from '../layouts/MainLayout.vue';
import CartHeader from '../components/cart/CartHeader.vue';
import AddProductForm from '../components/cart/AddProductForm.vue';
import CartItemsTable from '../components/cart/CartItemsTable.vue';
import CartSummary from '../components/cart/CartSummary.vue';
import { useCart } from '../composables/useCart';

const {
  userId,
  cartId,
  cart,
  total,
  isLoading,
  isSubmitting,
  errorMessage,
  successMessage,
  items,
  hasCart,
  setUserId,
  createOrLoadCart,
  fetchCart,
  addProduct,
  updateItemQuantity,
  removeItem
} = useCart();

const isBusy = computed(() => isLoading.value || isSubmitting.value);

function handleUserIdChange(value) {
  setUserId(value);
}

function handleAddProduct(payload) {
  if (!payload.name || payload.productId < 1 || payload.quantity < 1 || payload.price < 0) {
    return;
  }

  addProduct(payload);
}

function handleUpdateQuantity(itemId, quantity) {
  if (quantity < 1) {
    return;
  }

  updateItemQuantity(itemId, quantity);
}
</script>
