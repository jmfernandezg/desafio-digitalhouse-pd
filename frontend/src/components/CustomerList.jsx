import React from 'react';

function CustomerList({ customers, onEdit, onDelete }) {
    return (
        <div className="admin-list">
            <table>
                <thead>
                <tr>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {customers.map((customer) => (
                    <tr key={customer.id}>
                        <td>{customer.username}</td>
                        <td>{customer.email}</td>
                        <td>
                            <button onClick={() => onEdit(customer)}>Edit</button>
                            <button onClick={() => onDelete(customer.id)}>Delete</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default CustomerList;