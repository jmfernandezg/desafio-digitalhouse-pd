import { apiClient } from './client.jsx';
import { API_CONFIG } from '../config/constants';

export const LodgingService = {
    getCategories: () =>
        apiClient.get(API_CONFIG.ENDPOINTS.LODGING.CATEGORIES)
            .then(response => response.data),

    getCities: () =>
        apiClient.get(API_CONFIG.ENDPOINTS.LODGING.CITIES)
            .then(response => response.data),

    getAllLodgings: () =>
        apiClient.get(API_CONFIG.ENDPOINTS.LODGING.ALL)
            .then(response => response.data),

    getLodgingsByCategory: (category) =>
        apiClient.get(API_CONFIG.ENDPOINTS.LODGING.BY_CATEGORY.replace('{category}', category))
            .then(response => response.data),

    getLodgingById: (id) =>
        apiClient.get(API_CONFIG.ENDPOINTS.LODGING.BY_ID.replace('{id}', id))
            .then(response => response.data),

    deleteLodging: (id) =>
        apiClient.delete(API_CONFIG.ENDPOINTS.LODGING.BY_ID.replace('{id}', id))
            .then(response => response.data),

    search: (searchRequest) =>
        apiClient.post(API_CONFIG.ENDPOINTS.LODGING.SEARCH, searchRequest)
            .then(response => response.data),


};
