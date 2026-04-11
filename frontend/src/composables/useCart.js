import { computed, onMounted } from 'vue';
import { storeToRefs } from 'pinia';
import { useCartStore } from '../stores/cartStore';

export function useCart() {
  const cartStore = useCartStore();
  const { userId, cartId, cart, total, isLoading, isSubmitting, errorMessage, successMessage } =
    storeToRefs(cartStore);

  onMounted(() => {
    cartStore.initializeCart();
  });

  return {
    userId,
    cartId,
    cart,
    total,
    isLoading,
    isSubmitting,
    errorMessage,
    successMessage,
    items: computed(() => cartStore.items),
    hasCart: computed(() => cartStore.hasCart),
    setUserId: cartStore.setUserId,
    createOrLoadCart: cartStore.createOrLoadCart,
    fetchCart: cartStore.fetchCart,
    addProduct: cartStore.addProduct,
    updateItemQuantity: cartStore.updateItemQuantity,
    removeItem: cartStore.removeItem,
    clearFeedback: cartStore.clearFeedback
  };
}
