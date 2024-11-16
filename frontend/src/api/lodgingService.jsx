import { apiClient } from './client.jsx';
import { API_CONFIG } from '../config/constants';

export const LodgingService = {
    getCategories: () =>
        apiClient.get(API_CONFIG.ENDPOINTS.LODGING.CATEGORIES)
            .then(response => response.data),

    getAllLodgings: () =>
        apiClient.get(API_CONFIG.ENDPOINTS.LODGING.ALL)
            .then(response => response.data),
};