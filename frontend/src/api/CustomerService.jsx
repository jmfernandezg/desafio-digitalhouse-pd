import { apiClient } from './client.jsx';

const CustomerService = {
    login: (loginRequest) =>
        apiClient.post('/customer/login', loginRequest)
            .then(response => response.data),

    createCustomer: (customerCreationRequest) =>
        apiClient.post('/customer', customerCreationRequest)
            .then(response => response.data),

    deleteCustomer: (id) =>
        apiClient.delete('/customer', { params: { id } })
            .then(response => response.data),

    updateCustomer: (customer) =>
        apiClient.put('/customer', customer)
            .then(response => response.data),

    getAllCustomers: () =>
        apiClient.get('/customer')
            .then(response => response.data)
};

export default CustomerService;