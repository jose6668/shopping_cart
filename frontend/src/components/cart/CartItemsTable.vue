<template>
  <section class="panel card-panel">
    <div class="panel-header">
      <h2>Productos en el Carrito</h2>
      <p>Actualiza cantidades o elimina productos individuales.</p>
    </div>

    <div v-if="!items.length" class="empty-state">
      <h3>El carrito aún está vacío</h3>
      <p>Agrega un producto desde el formulario para comenzar a construir el pedido.</p>
    </div>

    <div v-else class="table-wrap">
      <table class="cart-table">
        <thead>
          <tr>
            <th>Producto</th>
            <th>Product ID</th>
            <th>Precio</th>
            <th>Cantidad</th>
            <th>Subtotal</th>
            <th>Acción</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in items" :key="item.id">
            <td>{{ item.name }}</td>
            <td>#{{ item.productId }}</td>
            <td>{{ formatCurrency(item.price) }}</td>
            <td>
              <div class="quantity-control">
                <input
                  :value="quantityDrafts[item.id] ?? item.quantity"
                  type="number"
                  min="1"
                  @input="setDraft(item.id, $event.target.value)"
                />
                <button
                  class="button button--ghost"
                  :disabled="disabled"
                  @click="emitUpdate(item)"
                >
                  Guardar
                </button>
              </div>
            </td>
            <td>{{ formatCurrency(item.subtotal) }}</td>
            <td>
              <button
                class="button button--danger"
                :disabled="disabled"
                @click="$emit('remove-item', item.id)"
              >
                Eliminar
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { reactive } from 'vue';
import { formatCurrency } from '../../utils/formatters';

defineProps({
  items: {
    type: Array,
    default: () => []
  },
  disabled: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['update-quantity', 'remove-item']);
const quantityDrafts = reactive({});

function setDraft(itemId, value) {
  quantityDrafts[itemId] = value;
}

function emitUpdate(item) {
  const nextQuantity = Number(quantityDrafts[item.id] ?? item.quantity);
  emit('update-quantity', item.id, nextQuantity);
}
</script>
