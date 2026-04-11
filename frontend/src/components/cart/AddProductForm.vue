<template>
  <section class="panel card-panel">
    <div class="panel-header">
      <h2>Agregar Producto</h2>
      <p>Envía el producto al carrito usando las rutas del gateway.</p>
    </div>

    <form class="form-grid" @submit.prevent="handleSubmit">
      <label class="field field--full">
        <span>Nombre</span>
        <input v-model.trim="form.name" type="text" placeholder="Mouse gamer" required />
      </label>

      <label class="field">
        <span>Product ID</span>
        <input v-model.number="form.productId" type="number" min="1" placeholder="10" required />
      </label>

      <label class="field">
        <span>Precio</span>
        <input
          v-model.number="form.price"
          type="number"
          min="0"
          step="0.01"
          placeholder="85000"
          required
        />
      </label>

      <label class="field field--compact">
        <span>Cantidad</span>
        <input v-model.number="form.quantity" type="number" min="1" placeholder="1" required />
      </label>

      <p v-if="validationMessage" class="feedback feedback--error">
        {{ validationMessage }}
      </p>

      <div class="form-actions">
        <button class="button button--primary" :disabled="disabled" type="submit">
          Agregar
        </button>
      </div>
    </form>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue';

const emit = defineEmits(['add-product']);

defineProps({
  disabled: {
    type: Boolean,
    default: false
  }
});

const form = reactive({
  productId: 1,
  name: '',
  quantity: 1,
  price: 0
});
const validationMessage = ref('');

function handleSubmit() {
  validationMessage.value = '';

  if (!form.name || Number(form.productId) < 1 || Number(form.quantity) < 1 || Number(form.price) < 0) {
    validationMessage.value = 'Completa todos los campos con valores válidos antes de agregar el producto.';
    return;
  }

  emit('add-product', {
    productId: Number(form.productId),
    name: form.name,
    quantity: Number(form.quantity),
    price: Number(form.price)
  });

  form.productId = Number(form.productId) + 1;
  form.name = '';
  form.quantity = 1;
  form.price = 0;
}
</script>
