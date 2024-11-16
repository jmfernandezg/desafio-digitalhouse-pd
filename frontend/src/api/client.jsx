import axios from 'axios';
import { API_CONFIG } from '../config/constants';

export const apiClient = axios.create({
    baseURL: API_CONFIG.BASE_URL,
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json'
    }
});

apiClient.interceptors.response.use(
    response => response,
    error => {
        console.error('API Error:', error);
        throw error;
    }
);