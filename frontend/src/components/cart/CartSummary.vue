<template>
  <section class="panel card-panel summary-panel">
    <div class="panel-header">
      <h2>Resumen del Carrito</h2>
      <p>Información consolidada retornada por backend.</p>
    </div>

    <div class="summary-panel__list">
      <article v-for="item in items" :key="item.id" class="summary-item">
        <div>
          <h3>{{ item.name }}</h3>
          <p>Precio: {{ formatCurrency(item.price) }}</p>
          <p>Cantidad: {{ item.quantity }}</p>
          <p>Subtotal: <strong>{{ formatCurrency(item.subtotal) }}</strong></p>
        </div>
        <button
          class="button button--danger"
          :disabled="disabled"
          @click="$emit('remove-item', item.id)"
        >
          Eliminar
        </button>
      </article>
    </div>

    <div class="summary-total">
      <div>
        <span>Total de unidades</span>
        <strong>{{ total?.totalItems ?? 0 }}</strong>
      </div>
      <div>
        <span>Total del carrito</span>
        <strong>{{ formatCurrency(total?.totalAmount ?? cart?.total ?? 0) }}</strong>
      </div>
      <div>
        <span>Última actualización</span>
        <strong>{{ formatDate(cart?.updatedAt) }}</strong>
      </div>
    </div>

    <button class="button button--primary button--full" :disabled="disabled" @click="$emit('refresh')">
      Consultar total
    </button>
  </section>
</template>

<script setup>
import { formatCurrency, formatDate } from '../../utils/formatters';

defineProps({
  items: {
    type: Array,
    default: () => []
  },
  total: {
    type: Object,
    default: null
  },
  cart: {
    type: Object,
    default: null
  },
  disabled: {
    type: Boolean,
    default: false
  }
});

defineEmits(['remove-item', 'refresh']);
</script>
