import React from "react";
import { AlertTriangle, X } from "lucide-react";

const ConfirmDialog = ({ open, onClose, onConfirm, title, description }) => {
    if (!open) return null;

    return (
        <>
            {/* Overlay */}
            <div
                className="fixed inset-0 bg-black/50 backdrop-blur-sm transition-opacity"
                onClick={onClose}
            />

            {/* Dialog */}
            <div className="fixed left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 w-full max-w-md">
                <div className="bg-white rounded-lg shadow-xl border border-gray-200 overflow-hidden animate-in zoom-in-95">
                    {/* Header */}
                    <div className="p-6 bg-red-50">
                        <div className="flex items-center gap-4">
                            <div className="flex-shrink-0">
                                <AlertTriangle className="h-8 w-8 text-red-500" />
                            </div>
                            <div className="flex-1">
                                <h3 className="text-lg font-semibold text-gray-900">
                                    {title || "Confirme la eliminación"}
                                </h3>
                                <p className="mt-2 text-sm text-gray-600">
                                    {description || "Esta acción no se puede deshacer. ¿Está seguro de que desea eliminar este registro?"}
                                </p>
                            </div>
                            <button
                                onClick={onClose}
                                className="text-gray-400 hover:text-gray-500 transition-colors"
                            >
                                <X className="h-5 w-5" />
                            </button>
                        </div>
                    </div>

                    {/* Footer */}
                    <div className="px-6 py-4 bg-gray-50 flex justify-end gap-3">
                        <button
                            onClick={onClose}
                            className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300
                         rounded-lg hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2
                         focus:ring-blue-500 transition-colors"
                        >
                            Cancelar
                        </button>
                        <button
                            onClick={onConfirm}
                            className="px-4 py-2 text-sm font-medium text-white bg-red-600 rounded-lg
                         hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2
                         focus:ring-red-500 transition-colors"
                        >
                            Si, eliminar
                        </button>
                    </div>
                </div>
            </div>
        </>
    );
};

export default ConfirmDialog;