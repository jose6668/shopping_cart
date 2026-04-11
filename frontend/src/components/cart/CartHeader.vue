<template>
  <section class="panel hero-panel">
    <div class="hero-panel__title-wrap">
      <div class="hero-panel__icon">🛒</div>
      <div>
        <p class="eyebrow">Frontend Vue + API Gateway</p>
        <h1>Carrito de Compras</h1>
        <p class="hero-panel__subtitle">
          Gestiona productos, cantidades y total consolidado desde una sola vista.
        </p>
      </div>
    </div>

    <div class="hero-panel__meta">
      <label class="field">
        <span>Usuario</span>
        <input
          :value="userId"
          type="number"
          min="1"
          placeholder="Ej. 123"
          @input="$emit('update:userId', $event.target.value)"
        />
      </label>

      <div class="hero-panel__status">
        <span>Cart ID</span>
        <strong>{{ cartId ?? 'Sin crear' }}</strong>
      </div>

      <button class="button button--primary" :disabled="disabled" @click="$emit('load-cart')">
        {{ cartId ? 'Recargar carrito' : 'Crear o cargar carrito' }}
      </button>
    </div>
  </section>
</template>

<script setup>
defineProps({
  userId: {
    type: [Number, String],
    required: true
  },
  cartId: {
    type: Number,
    default: null
  },
  disabled: {
    type: Boolean,
    default: false
  }
});

defineEmits(['update:userId', 'load-cart']);
</script>
