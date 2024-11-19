import React, { useState } from 'react';
import { Edit, Trash2, ChevronLeft, ChevronRight } from 'lucide-react';
import ConfirmDialog from './ConfirmDialog';

function LodgingList({ lodgings, onEdit, onDelete }) {
    const [currentPage, setCurrentPage] = useState(1);
    const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
    const [lodgingToDelete, setLodgingToDelete] = useState(null);
    const lodgingsPerPage = 5;

    const handleDeleteClick = (lodging) => {
        setLodgingToDelete(lodging);
        setOpenConfirmDialog(true);
    };

    const handleConfirmDelete = () => {
        onDelete(lodgingToDelete.id);
        setOpenConfirmDialog(false);
        setLodgingToDelete(null);
    };

    // Pagination
    const totalPages = Math.ceil(lodgings.length / lodgingsPerPage);
    const startIndex = (currentPage - 1) * lodgingsPerPage;
    const endIndex = startIndex + lodgingsPerPage;
    const currentLodgings = lodgings.slice(startIndex, endIndex);

    const formatPrice = (price) => {
        return new Intl.NumberFormat('es-AR', {
            style: 'currency',
            currency: 'ARS'
        }).format(price);
    };

    return (
        <div className="bg-white rounded-lg shadow-sm">
            {/* Table */}
            <div className="overflow-x-auto">
                <table className="w-full">
                    <thead>
                    <tr className="bg-gray-50 border-b border-gray-200">
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Nombre
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Ciudad
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Precio
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Acciones
                        </th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                    {currentLodgings.map((lodging) => (
                        <tr
                            key={lodging.id}
                            className="hover:bg-gray-50 transition-colors duration-200"
                        >
                            <td className="px-6 py-4 whitespace-nowrap">
                                <div className="flex items-center">
                                    <div className="ml-4">
                                        <div className="text-sm font-medium text-gray-900">
                                            {lodging.name}
                                        </div>
                                        {lodging.category && (
                                            <div className="text-sm text-gray-500">
                                                {lodging.category}
                                            </div>
                                        )}
                                    </div>
                                </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                {lodging.city}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                  <span className="text-sm font-semibold text-gray-900">
                    {formatPrice(lodging.price)}
                  </span>
                                <span className="text-sm text-gray-500">
                    /noche
                  </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm">
                                <div className="flex gap-2">
                                    <button
                                        onClick={() => onEdit(lodging)}
                                        className="p-1 text-blue-600 hover:text-blue-800 transition-colors duration-200 rounded-lg hover:bg-blue-50"
                                        title="Editar hospedaje"
                                    >
                                        <Edit size={18} />
                                    </button>
                                    <button
                                        onClick={() => handleDeleteClick(lodging)}
                                        className="p-1 text-red-600 hover:text-red-800 transition-colors duration-200 rounded-lg hover:bg-red-50"
                                        title="Eliminar hospedaje"
                                    >
                                        <Trash2 size={18} />
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))}
                    {currentLodgings.length === 0 && (
                        <tr>
                            <td
                                colSpan={4}
                                className="px-6 py-8 text-center text-gray-500 text-sm"
                            >
                                No hay hospedajes para mostrar
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>

            {/* Pagination Controls */}
            {lodgings.length > lodgingsPerPage && (
                <div className="flex items-center justify-between px-6 py-3 border-t border-gray-200 bg-gray-50">
                    <div className="flex items-center text-sm text-gray-700">
            <span>
              Mostrando {startIndex + 1}-{Math.min(endIndex, lodgings.length)} de {lodgings.length} hospedajes
            </span>
                    </div>
                    <div className="flex items-center gap-2">
                        <button
                            onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                            disabled={currentPage === 1}
                            className={`p-2 rounded-lg transition-colors duration-200
                ${currentPage === 1
                                ? 'text-gray-400 cursor-not-allowed'
                                : 'text-gray-700 hover:bg-gray-200'
                            }`}
                            aria-label="Página anterior"
                        >
                            <ChevronLeft size={20} />
                        </button>

                        <div className="flex items-center gap-1">
                            {[...Array(totalPages)].map((_, i) => (
                                <button
                                    key={i + 1}
                                    onClick={() => setCurrentPage(i + 1)}
                                    className={`min-w-[2rem] h-8 rounded-lg text-sm font-medium transition-colors duration-200
                    ${currentPage === i + 1
                                        ? 'bg-blue-600 text-white'
                                        : 'text-gray-700 hover:bg-gray-200'
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
                                : 'text-gray-700 hover:bg-gray-200'
                            }`}
                            aria-label="Página siguiente"
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

export default LodgingList;