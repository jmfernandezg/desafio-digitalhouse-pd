import React, { useState } from 'react';
import { Edit, Trash2, Search, ChevronDown, Mail, Phone } from 'lucide-react';
import ConfirmDialog from './ConfirmDialog';

const CustomerList = ({ customers, onEdit, onDelete }) => {
    const [searchTerm, setSearchTerm] = useState('');
    const [sortField, setSortField] = useState('lastName');
    const [sortDirection, setSortDirection] = useState('asc');
    const [selectedCustomer, setSelectedCustomer] = useState(null);
    const [showConfirmDialog, setShowConfirmDialog] = useState(false);

    const handleSort = (field) => {
        if (sortField === field) {
            setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc');
        } else {
            setSortField(field);
            setSortDirection('asc');
        }
    };

    const filteredCustomers = customers
        .filter(customer =>
            customer.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
            customer.lastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
            customer.email.toLowerCase().includes(searchTerm.toLowerCase())
        )
        .sort((a, b) => {
            const modifier = sortDirection === 'asc' ? 1 : -1;
            return a[sortField] > b[sortField] ? modifier : -modifier;
        });

    const SortableHeader = ({ field, children }) => (
        <th
            className="px-6 py-3 cursor-pointer group hover:bg-gray-50"
            onClick={() => handleSort(field)}
        >
            <div className="flex items-center gap-2">
                {children}
                <ChevronDown
                    className={`w-4 h-4 text-gray-400 transition-transform
            ${sortField === field && sortDirection === 'desc' ? 'rotate-180' : ''} 
            ${sortField !== field ? 'opacity-0 group-hover:opacity-100' : ''}`}
                />
            </div>
        </th>
    );

    return (
        <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            {/* Search Bar */}
            <div className="p-4 border-b border-gray-200">
                <div className="relative">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 h-5 w-5" />
                    <input
                        type="text"
                        placeholder="Buscar clientes..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none
                     focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    />
                </div>
            </div>

            {/* Table */}
            <div className="overflow-x-auto">
                <table className="w-full">
                    <thead className="bg-gray-50 text-left text-sm text-gray-500">
                    <tr>
                        <SortableHeader field="firstName">Nombre</SortableHeader>
                        <SortableHeader field="lastName">Apellido</SortableHeader>
                        <SortableHeader field="email">Email</SortableHeader>
                        <th className="px-6 py-3">Acciones</th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                    {filteredCustomers.map((customer) => (
                        <tr
                            key={customer.id}
                            className="hover:bg-gray-50 text-sm text-gray-600"
                        >
                            <td className="px-6 py-4">{customer.firstName}</td>
                            <td className="px-6 py-4">{customer.lastName}</td>
                            <td className="px-6 py-4">
                                <div className="flex items-center gap-2">
                                    <Mail className="h-4 w-4 text-gray-400" />
                                    {customer.email}
                                </div>
                            </td>
                            <td className="px-6 py-4">
                                <div className="flex items-center gap-3">
                                    <button
                                        onClick={() => onEdit(customer)}
                                        className="p-1 text-blue-600 hover:bg-blue-50 rounded transition-colors"
                                    >
                                        <Edit className="h-4 w-4" />
                                    </button>
                                    <button
                                        onClick={() => {
                                            setSelectedCustomer(customer);
                                            setShowConfirmDialog(true);
                                        }}
                                        className="p-1 text-red-600 hover:bg-red-50 rounded transition-colors"
                                    >
                                        <Trash2 className="h-4 w-4" />
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            {/* Empty State */}
            {filteredCustomers.length === 0 && (
                <div className="text-center py-12">
                    <p className="text-gray-500">No se encontraron clientes</p>
                </div>
            )}

            {/* Confirm Dialog */}
            <ConfirmDialog
                open={showConfirmDialog}
                onClose={() => setShowConfirmDialog(false)}
                onConfirm={() => {
                    onDelete(selectedCustomer);
                    setShowConfirmDialog(false);
                    setSelectedCustomer(null);
                }}
                title="Eliminar Cliente"
                description={`¿Está seguro de que desea eliminar a ${selectedCustomer?.firstName} ${selectedCustomer?.lastName}?`}
            />
        </div>
    );
};

export default CustomerList;