import httpClient from '../api/httpClient';

export function createCart(payload) {
  return httpClient.post('/api/v1/carts', payload);
}

export function getCartById(cartId) {
  return httpClient.get(`/api/v1/carts/${cartId}`);
}

export function getCartTotal(cartId) {
  return httpClient.get(`/api/v1/carts/${cartId}/total`);
}

export function addItemToCart(cartId, payload) {
  return httpClient.post(`/api/v1/carts/${cartId}/items`, payload);
}

export function updateCartItemQuantity(cartId, itemId, payload) {
  return httpClient.put(`/api/v1/carts/${cartId}/items/${itemId}`, payload);
}

export function deleteCartItem(cartId, itemId) {
  return httpClient.delete(`/api/v1/carts/${cartId}/items/${itemId}`);
}
