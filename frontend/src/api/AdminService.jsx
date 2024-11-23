import { apiClient } from './client.jsx';

/**
 * Service for administrative operations
 * Requires admin authentication
 */
const AdminService = {
    /**
     * Gets all customers
     * @returns {Promise<Object>} List of all customers
     */
    getAllCustomers: () => apiClient.get('/api/admin/customers')
        .then(response => response.data),

    /**
     * Deletes a customer
     * @param {number} id - Customer ID to delete
     * @returns {Promise<void>}
     */
    deleteCustomer: (id) => apiClient.delete(`/api/admin/customers/${id}`)
        .then(response => response.data),

    /**
     * Gets customers by country
     * @param {string} country - Country name
     * @returns {Promise<Object>} List of customers in the specified country
     */
    getCustomersByCountry: (country) => apiClient.get(`/api/admin/customers/by-country/${country}`)
        .then(response => response.data),

    /**
     * Gets customers with expiring passports
     * @param {string} beforeDate - ISO date string
     * @returns {Promise<Object>} List of customers with expiring passports
     */
    getCustomersWithExpiringPassports: (beforeDate) =>
        apiClient.get('/api/admin/customers/expiring-passports', {
            params: { beforeDate }
        })
            .then(response => response.data),

    /**
     * Gets customer statistics
     * @returns {Promise<Object>} Customer statistics
     */
    getCustomerStatistics: () => apiClient.get('/api/admin/customers/statistics')
        .then(response => response.data),

    /**
     * Error handler helper
     * @param {Error} error - Error object
     * @throws {Error} Processed error with meaningful message
     */
    handleError: (error) => {
        const message = error.response?.data?.error ||
            error.response?.data?.message ||
            'Error en la operación';
        throw new Error(message);
    }
};

// Error interceptor for authentication
apiClient.interceptors.response.use(
    response => response,
    error => {
        if (error.response?.status === 401) {
            // Handle unauthorized access - could redirect to login
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default AdminService;