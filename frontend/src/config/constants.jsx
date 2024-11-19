export const API_CONFIG = {
    BASE_URL: process.env.NODE_ENV === 'development'
        ? 'http://localhost:8080/v1'
        : '/api/v1',
    ENDPOINTS: {
        LODGING: {
            ALL: '/lodging',
            CATEGORIES: '/lodging/categories',
            CITIES: '/lodging/cities',
            BY_CATEGORY: '/lodging/categories/{category}',
            BY_ID: '/lodging/{id}',
            SEARCH: '/lodging/search'
        }
    }
};
