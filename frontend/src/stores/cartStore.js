import { defineStore } from 'pinia';
import {
  addItemToCart,
  createCart,
  deleteCartItem,
  getCartById,
  getCartTotal,
  updateCartItemQuantity
} from '../services/cartService';

const STORAGE_KEY = 'shopping-cart-ui-state';

function readPersistedState() {
  try {
    const savedState = localStorage.getItem(STORAGE_KEY);
    return savedState ? JSON.parse(savedState) : {};
  } catch {
    return {};
  }
}

function persistState(state) {
  localStorage.setItem(
    STORAGE_KEY,
    JSON.stringify({
      userId: state.userId,
      cartId: state.cartId
    })
  );
}

export const useCartStore = defineStore('cart', {
  state: () => {
    const savedState = readPersistedState();

    return {
      userId: savedState.userId ?? 123,
      cartId: savedState.cartId ?? null,
      cart: null,
      total: null,
      isLoading: false,
      isSubmitting: false,
      errorMessage: '',
      successMessage: ''
    };
  },
  getters: {
    items(state) {
      return state.cart?.items ?? [];
    },
    hasCart(state) {
      return Boolean(state.cartId);
    }
  },
  actions: {
    setUserId(userId) {
      const parsedUserId = Number(userId);
      this.userId = Number.isFinite(parsedUserId) && parsedUserId > 0 ? parsedUserId : '';
      persistState(this);
    },
    clearFeedback() {
      this.errorMessage = '';
      this.successMessage = '';
    },
    setError(message) {
      this.errorMessage = message;
      this.successMessage = '';
    },
    setSuccess(message) {
      this.successMessage = message;
      this.errorMessage = '';
    },
    async initializeCart() {
      if (!this.cartId) {
        return;
      }

      await this.fetchCart(this.cartId);
    },
    async createOrLoadCart() {
      this.clearFeedback();

      if (!Number.isFinite(Number(this.userId)) || Number(this.userId) <= 0) {
        this.setError('Ingresa un userId válido antes de crear el carrito.');
        return;
      }

      this.isSubmitting = true;

      try {
        const response = await createCart({ userId: Number(this.userId) });
        this.cartId = response.data.id;
        persistState(this);
        await this.fetchCart(this.cartId);
        this.setSuccess('Carrito cargado correctamente.');
      } catch (error) {
        this.setError(error.message);
      } finally {
        this.isSubmitting = false;
      }
    },
    async fetchCart(cartId = this.cartId) {
      if (!cartId) {
        return;
      }

      this.clearFeedback();
      this.isLoading = true;

      try {
        const [cartResponse, totalResponse] = await Promise.all([
          getCartById(cartId),
          getCartTotal(cartId)
        ]);

        this.cartId = cartResponse.data.id;
        this.cart = cartResponse.data;
        this.total = totalResponse.data;
        this.userId = cartResponse.data.userId;
        persistState(this);
      } catch (error) {
        this.setError(error.message);
      } finally {
        this.isLoading = false;
      }
    },
    async addProduct(payload) {
      if (!this.cartId) {
        this.setError('Primero debes crear o cargar un carrito.');
        return;
      }

      this.clearFeedback();
      this.isSubmitting = true;

      try {
        await addItemToCart(this.cartId, payload);
        await this.fetchCart(this.cartId);
        this.setSuccess('Producto agregado al carrito.');
      } catch (error) {
        this.setError(error.message);
      } finally {
        this.isSubmitting = false;
      }
    },
    async updateItemQuantity(itemId, quantity) {
      this.clearFeedback();
      this.isSubmitting = true;

      try {
        await updateCartItemQuantity(this.cartId, itemId, { quantity });
        await this.fetchCart(this.cartId);
        this.setSuccess('Cantidad actualizada correctamente.');
      } catch (error) {
        this.setError(error.message);
      } finally {
        this.isSubmitting = false;
      }
    },
    async removeItem(itemId) {
      this.clearFeedback();
      this.isSubmitting = true;

      try {
        await deleteCartItem(this.cartId, itemId);
        await this.fetchCart(this.cartId);
        this.setSuccess('Producto eliminado del carrito.');
      } catch (error) {
        this.setError(error.message);
      } finally {
        this.isSubmitting = false;
      }
    }
  }
});
