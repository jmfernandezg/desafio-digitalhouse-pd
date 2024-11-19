import React, { useState } from 'react';
import { Edit, Trash2, ChevronLeft, ChevronRight } from 'lucide-react';
import ConfirmDialog from './ConfirmDialog';

function CustomerList({ customers, onEdit, onDelete }) {
    const [currentPage, setCurrentPage] = useState(1);
    const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
    const [customerToDelete, setCustomerToDelete] = useState(null);
    const customersPerPage = 5;

    const handleDeleteClick = (customer) => {
        setCustomerToDelete(customer);
        setOpenConfirmDialog(true);
    };

    const handleConfirmDelete = () => {
        onDelete(customerToDelete.id);
        setOpenConfirmDialog(false);
        setCustomerToDelete(null);
    };

    // Pagination
    const totalPages = Math.ceil(customers.length / customersPerPage);
    const startIndex = (currentPage - 1) * customersPerPage;
    const endIndex = startIndex + customersPerPage;
    const currentCustomers = customers.slice(startIndex, endIndex);

    return (
        <div className="bg-white rounded-lg shadow-sm">
            {/* Table */}
            <div className="overflow-x-auto">
                <table className="w-full">
                    <thead>
                    <tr className="bg-gray-50 border-b border-gray-200">
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Usuario
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Email
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Acciones
                        </th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                    {currentCustomers.map((customer) => (
                        <tr
                            key={customer.id}
                            className="hover:bg-gray-50 transition-colors duration-200"
                        >
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                {customer.username}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                {customer.email}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm">
                                <div className="flex gap-2">
                                    <button
                                        onClick={() => onEdit(customer)}
                                        className="p-1 text-blue-600 hover:text-blue-800 transition-colors duration-200"
                                        title="Editar"
                                    >
                                        <Edit size={18} />
                                    </button>
                                    <button
                                        onClick={() => handleDeleteClick(customer)}
                                        className="p-1 text-red-600 hover:text-red-800 transition-colors duration-200"
                                        title="Eliminar"
                                    >
                                        <Trash2 size={18} />
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))}
                    {currentCustomers.length === 0 && (
                        <tr>
                            <td
                                colSpan={3}
                                className="px-6 py-8 text-center text-gray-500 text-sm"
                            >
                                No hay clientes para mostrar
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>

            {/* Pagination */}
            {customers.length > customersPerPage && (
                <div className="flex items-center justify-between px-6 py-3 border-t border-gray-200">
                    <div className="flex items-center gap-2 text-sm text-gray-700">
            <span>
              Mostrando {startIndex + 1}-{Math.min(endIndex, customers.length)} de {customers.length}
            </span>
                    </div>
                    <div className="flex gap-2">
                        <button
                            onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                            disabled={currentPage === 1}
                            className={`p-2 rounded-lg transition-colors duration-200
                ${currentPage === 1
                                ? 'text-gray-400 cursor-not-allowed'
                                : 'text-gray-700 hover:bg-gray-100'
                            }`}
                        >
                            <ChevronLeft size={20} />
                        </button>
                        {/* Page Numbers */}
                        <div className="flex items-center gap-1">
                            {[...Array(totalPages)].map((_, i) => (
                                <button
                                    key={i + 1}
                                    onClick={() => setCurrentPage(i + 1)}
                                    className={`min-w-[2rem] h-8 rounded-lg text-sm transition-colors duration-200
                    ${currentPage === i + 1
                                        ? 'bg-blue-600 text-white'
                                        : 'text-gray-700 hover:bg-gray-100'
                                    }`}
                                >
                                    {i + 1}
                                </button>
                            ))}
                        </div>
                        <button
                            onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
                            disabled={currentPage === totalPages}
                            className={`p-2 rounded-lg transition-colors duration-200
                ${currentPage === totalPages
                                ? 'text-gray-400 cursor-not-allowed'
                                : 'text-gray-700 hover:bg-gray-100'
                            }`}
                        >
                            <ChevronRight size={20} />
                        </button>
                    </div>
                </div>
            )}

            {/* Confirm Dialog */}
            {openConfirmDialog && (
                <ConfirmDialog
                    open={openConfirmDialog}
                    onClose={() => setOpenConfirmDialog(false)}
                    onConfirm={handleConfirmDelete}
                />
            )}
        </div>
    );
}

export default CustomerList;