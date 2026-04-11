import axios from 'axios';

const httpClient = axios.create({
  baseURL: import.meta.env.VITE_GATEWAY_URL || 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json'
  }
});

httpClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const apiMessage =
      error.response?.data?.message ||
      error.response?.data?.error ||
      error.response?.data?.details ||
      error.message ||
      'No fue posible completar la solicitud.';

    return Promise.reject(new Error(apiMessage));
  }
);

export default httpClient;
