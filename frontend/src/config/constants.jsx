export const API_CONFIG = {
    BASE_URL: process.env.NODE_ENV === 'development'
        ? 'http://localhost:8080/v1'
        : '/api/v1',
    ENDPOINTS: {
        LODGING: {
            CATEGORIES: '/lodging/categories',
            ALL: '/lodging'
        }
    }
};
