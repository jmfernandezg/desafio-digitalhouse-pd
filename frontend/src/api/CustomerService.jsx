import { apiClient } from './client.jsx';

/**
 * Service for customer-facing operations
 * Handles authentication, registration, and profile management
 */
const CustomerService = {
    /**
     * Authenticates a user
     * @param {Object} loginRequest - {email, password}
     * @returns {Promise<Object>} Login response with auth token
     */
    login: (loginRequest) => apiClient.post('/api/customers/login', loginRequest)
        .then(response => response.data),

    /**
     * Registers a new customer
     * @param {Object} customerCreationRequest - Customer registration data
     * @returns {Promise<Object>} Created customer details
     */
    register: (customerCreationRequest) => apiClient.post('/api/customers/register', customerCreationRequest)
        .then(response => response.data),

    /**
     * Updates customer profile
     * @param {number} id - Customer ID
     * @param {Object} updateRequest - Customer update data
     * @returns {Promise<Object>} Updated customer details
     */
    updateProfile: (id, updateRequest) => apiClient.put(`/api/customers/${id}`, updateRequest)
        .then(response => response.data)
};

export default CustomerService;
