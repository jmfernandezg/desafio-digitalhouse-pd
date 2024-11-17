import React, { useState } from 'react';
import ReactPaginate from 'react-paginate';

function CustomerList({ customers, onEdit, onDelete }) {
    const [currentPage, setCurrentPage] = useState(0);
    const customersPerPage = 5;

    const handlePageClick = (data) => {
        setCurrentPage(data.selected);
    };

    const startIndex = currentPage * customersPerPage;
    const currentCustomers = customers.slice(startIndex, startIndex + customersPerPage);

    return (
        <div className="admin-list">
            <table>
                <thead>
                <tr>
                    <th>Usuario</th>
                    <th>Email</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                {currentCustomers.map((customer) => (
                    <tr key={customer.id}>
                        <td>{customer.username}</td>
                        <td>{customer.email}</td>
                        <td>
                            <button onClick={() => onEdit(customer)}>Editar</button>
                            <button onClick={() => onDelete(customer.id)}>Eliminar</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <ReactPaginate
                previousLabel={'Anterior'}
                nextLabel={'Siguiente'}
                breakLabel={'...'}
                pageCount={Math.ceil(customers.length / customersPerPage)}
                marginPagesDisplayed={2}
                pageRangeDisplayed={5}
                onPageChange={handlePageClick}
                containerClassName={'pagination'}
                activeClassName={'active'}
            />
        </div>
    );
}

export default CustomerList;